package com.docusign.jwtconsoleapp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.client.auth.OAuth.OAuthToken;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.client.auth.OAuth.Account;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 * Starter class for JWTConsoleApp application.
 */

public class JWTConsoleApp {

    /**
     * Application entry point.
     *
     * @param args application command line arguments
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System. in);
        System.out.println ("Welcome to the JWT Code example! ");
        System.out.print("Enter the signer's email address: ");
        String signerEmail = scanner. nextLine();
        System.out.print("Enter the signer's name: ");
        String signerName = scanner. nextLine();
        System.out.print("Enter the carbon copy's email address: ");
        String ccEmail = scanner. nextLine();
        System.out.print("Enter the carbon copy's name: ");
        String ccName = scanner. nextLine();
        
        try
        {
            // Get information fro app.config
            Properties prop = new Properties();
            String fileName = "app.config";
            FileInputStream fis = new FileInputStream(fileName);
            prop.load(fis);
            // Get access token and accountId
            ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
            apiClient.setOAuthBasePath("account-d.docusign.com");
            ArrayList<String> scopes = new ArrayList<String>();
            scopes.add("signature");
            scopes.add("impersonation");
            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));
            OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                prop.getProperty("clientId"),
                prop.getProperty("userId"),
                scopes,
                privateKeyBytes,
                3600);
            String accessToken = oAuthToken.getAccessToken();
            UserInfo userInfo = apiClient.getUserInfo(accessToken);
            String accountId = userInfo.getAccounts().get(0).getAccountId();

            // Create envelopeDefinition object
            EnvelopeDefinition envelope = new EnvelopeDefinition();
            envelope.setEmailSubject("Please sign this document set");
            envelope.setStatus("sent");

            // Set recipients
            Signer signer = new Signer();
            signer.setEmail(signerEmail);
            signer.setName(signerName);
            signer.recipientId("1");
            CarbonCopy cc = new CarbonCopy();
            cc.setEmail(ccEmail);
            cc.setName(ccName);
            cc.recipientId("2");
            Recipients recipients = new Recipients();
            recipients.setSigners(Arrays.asList(signer));
            recipients.setCarbonCopies(Arrays.asList(cc));
            envelope.setRecipients(recipients);
    
            // Add document
            Document document = new Document();
            document.setDocumentBase64("VGhhbmtzIGZvciByZXZpZXdpbmcgdGhpcyEKCldlJ2xsIG1vdmUgZm9yd2FyZCBhcyBzb29uIGFzIHdlIGhlYXIgYmFjay4=");
            document.setName("doc1.txt");
            document.setFileExtension("txt");
            document.setDocumentId("1");
            envelope.setDocuments(Arrays.asList(document));            

            // Send envelope
            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
            EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
            EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
            System.out.println("Successfully sent envelope with envelopeId " + results.getEnvelopeId());
        }
        catch (Exception e)
        {
            System.out.print ("Error!!!  ");
            System.out.print (e.getMessage());
        }
    }
}
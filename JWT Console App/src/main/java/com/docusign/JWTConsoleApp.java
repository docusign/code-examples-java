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
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
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
            Properties prop = new Properties();
            String fileName = "app.config";
            FileInputStream fis = new FileInputStream(fileName);
            prop.load(fis);
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

            EnvelopeDefinition envelope = new EnvelopeDefinition();
            envelope.setEmailSubject("Please sign this document set");
            envelope.setStatus("sent");

            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
            EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

            envelopesApi.createEnvelope(accountId, envelope);
    
        }
        catch (Exception e)
        {
            System.out.print ("Error!!!  ");
            System.out.print (e.getMessage());
        }
    }
}
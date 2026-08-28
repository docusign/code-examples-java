package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountIdentityVerificationWorkflow;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.RecipientIdentityInputOption;
import com.docusign.esign.model.RecipientIdentityPhoneNumber;
import com.docusign.esign.model.RecipientIdentityVerification;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ViewUrl;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Document;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FocusedViewService {

    private static String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static String DOCUMENT_NAME = "Lorem Ipsum";

    private static String SIGNER_CLIENT_ID = "1000";

    public static String STATE_123 = "?state=123";

    public static String AUTHENTICATION_METHOD = "none";

    public String[] sendEnvelopeWithFocusedView(
            String signerEmail,
            String signerName,
            String phoneNumber,
            String countryCode,
            ApiClient apiClient,
            String accountId,
            String returnUrl) throws ApiException, IOException {
        //ds-snippet-start:eSign44Step3
        String workflowId = null;

        // Resolve identity verification workflow only when phone auth is requested.
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            AccountsApi accountsApi = new AccountsApi(apiClient);
            var workflowRes = accountsApi.getAccountIdentityVerificationWithHttpInfo(
                accountId,
                accountsApi.new GetAccountIdentityVerificationOptions());
            
            Map<String, List<String>> headers = workflowRes.getHeaders();
            java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
            List<String> reset = headers.get("X-RateLimit-Reset");

            if (remaining != null & reset != null) {
                Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
                System.out.println("API calls remaining: " + remaining);
                System.out.println("Next Reset: " + resetInstant);
            }

            List<AccountIdentityVerificationWorkflow> identityVerification =
                    workflowRes.getData().getIdentityVerification();
            for (AccountIdentityVerificationWorkflow workflow : identityVerification) {
                if ("Phone Authentication".equals(workflow.getDefaultName())) {
                    workflowId = workflow.getWorkflowId();
                    break;
                }
            }

            if (workflowId == null) {
                throw new ApiException("IDENTITY_WORKFLOW_INVALID_ID");
            }
        }

        EnvelopeDefinition envelope = makeEnvelope(
                signerEmail,
                signerName,
                SIGNER_CLIENT_ID,
                workflowId,
                phoneNumber,
                countryCode,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        var envelopeSummary = envelopesApi.createEnvelopeWithHttpInfo(accountId, envelope,
                envelopesApi.new CreateEnvelopeOptions());
        Map<String, List<String>> headers = envelopeSummary.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        String envelopeId = envelopeSummary.getData().getEnvelopeId();
        //ds-snippet-end:eSign44Step3

        //ds-snippet-start:eSign44Step5
        RecipientViewRequest viewRequest = makeRecipientViewRequest(signerEmail, signerName, returnUrl,
                SIGNER_CLIENT_ID, returnUrl);
        var viewUrl = envelopesApi.createRecipientViewWithHttpInfo(accountId, envelopeId, viewRequest);
        headers = viewUrl.getHeaders();
        remaining = headers.get("X-RateLimit-Remaining");
        reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        String redirectUrl = viewUrl.getData().getUrl();

        return new String[] { envelopeId, redirectUrl };
        //ds-snippet-end:eSign44Step5
    }

    //ds-snippet-start:eSign44Step4
    public RecipientViewRequest makeRecipientViewRequest(
            String signerEmail, String signerName, String returnUrl, String signerClientId, String pingUrl)
            throws ApiException {
        String pingFrequency = "600";
        String linkToLauncher = "http://localhost:8080";
        String linkToDocuSignServer = "https://apps-d.docusign.com";

        RecipientViewRequest viewRequest = new RecipientViewRequest();

        viewRequest.setReturnUrl(returnUrl + STATE_123);
        viewRequest.setAuthenticationMethod(AUTHENTICATION_METHOD);
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(signerClientId);

        if (pingUrl != null) {
            viewRequest.setPingFrequency(pingFrequency);
            viewRequest.setPingUrl(pingUrl);
        }

        viewRequest.setFrameAncestors(Arrays.asList(new String[] { linkToLauncher, linkToDocuSignServer }));
        viewRequest.setMessageOrigins(Arrays.asList(new String[] { linkToDocuSignServer }));

        return viewRequest;
    }
    //ds-snippet-end:eSign44Step4

    //ds-snippet-start:eSign44Step2
    public EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String signerClientId,
            String workflowId,
            String phoneNumber,
            String countryCode,
            String documentFileName,
            String documentName) throws IOException {
        String emailSubject = "Please sign this document";
        String recipientId = "1";
        String docId = "3";

        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(signerClientId);
        signer.recipientId(recipientId);

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            RecipientIdentityPhoneNumber recipientIdentityPhoneNumber = new RecipientIdentityPhoneNumber();
            recipientIdentityPhoneNumber.setCountryCode(countryCode);
            recipientIdentityPhoneNumber.setNumber(phoneNumber);

            RecipientIdentityInputOption inputOption = new RecipientIdentityInputOption();
            inputOption.setName("phone_number_list");
            inputOption.setValueType("PhoneNumberList");
            inputOption.setPhoneNumberList(List.of(recipientIdentityPhoneNumber));

            RecipientIdentityVerification identityVerification = new RecipientIdentityVerification();
            identityVerification.setWorkflowId(workflowId);
            identityVerification.setInputOptions(List.of(inputOption));

            signer.setIdentityVerification(identityVerification);
        }

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject(emailSubject);
        envelopeDefinition.setRecipients(recipients);

        Document document = EnvelopeHelpers.createDocumentFromFile(documentFileName, documentName, docId);
        envelopeDefinition.setDocuments(Collections.singletonList(document));
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }
    //ds-snippet-end:eSign44Step2
}

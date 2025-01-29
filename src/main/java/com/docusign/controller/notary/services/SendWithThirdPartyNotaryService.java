package com.docusign.controller.notary.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.model.*;
import com.docusign.webforms.client.ApiException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public final class SendWithThirdPartyNotaryService {
    public static String sendWithNotary(String signerEmail, String signerName, String accountId,
            EnvelopesApi envelopesApi, String envStatus) throws ApiException, com.docusign.esign.client.ApiException {
        EnvelopeDefinition env = makeEnvelope(signerEmail, signerName, envStatus);

        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, env);
        return results.getEnvelopeId();
    }

    private static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName, String envStatus) {
        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setEmailSubject("Please sign this document set");

        env.setDocuments(getDocuments(signerEmail, signerName));

        Recipients recipients = new Recipients();
        recipients.setSigners(getSigners(signerEmail, signerName));
        recipients.setNotaries(getNotaryRecipients());

        env.setRecipients(recipients);
        env.setStatus(envStatus);

        return env;
    }

    private static java.util.List<Document> getDocuments(String signerEmail, String signerName) {
        Document doc1 = new Document();
        String b64 = java.util.Base64.getEncoder().encodeToString(getDocumentExample(signerEmail, signerName));
        doc1.setDocumentBase64(b64);
        doc1.setName("Order acknowledgement");
        doc1.setFileExtension("html");
        doc1.setDocumentId("1");

        return Collections.singletonList(doc1);
    }

    private static byte[] getDocumentExample(String signerEmail, String signerName) {
        String document = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "      <meta charset=\"UTF-8\">\n"
                + "    </head>\n"
                + "    <body style=\"font-family:sans-serif;margin-left:2em;\">\n"
                + "    <h1 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif; color: darkblue;margin-bottom: 0;\">World Wide Corp</h1>\n"
                + "    <h2 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif; margin-top: 0px;margin-bottom: 3.5em;font-size: 1em; color: darkblue;\">Order Processing Division</h2>\n"
                + "    <h4>Ordered by " + signerName + "</h4>\n"
                + "    <p>Email: " + signerEmail + "</p>\n"
                + "    <h3>Agreed: <span style=\"color:white;\">**signature_1**/</span></h3>\n"
                + "    </body>\n"
                + "</html>";
        return document.getBytes(StandardCharsets.UTF_8);
    }

    private static java.util.List<Signer> getSigners(String signerEmail, String signerName) {
        Signer signer = new Signer();
        signer.setClientUserId("1000");
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.setRecipientId("2");
        signer.setRoutingOrder("1");
        signer.setNotaryId("1");

        SignHere signHere1 = new SignHere();
        signHere1.setDocumentId("1");
        signHere1.setXPosition("200");
        signHere1.setYPosition("235");
        signHere1.setPageNumber("1");

        SignHere signHere2 = new SignHere();
        signHere2.setStampType("stamp");
        signHere2.setDocumentId("1");
        signHere2.setXPosition("200");
        signHere2.setYPosition("150");
        signHere2.setPageNumber("1");

        Tabs signerTabs = new Tabs();
        signerTabs.setSignHereTabs(java.util.Arrays.asList(signHere1, signHere2));
        signer.setTabs(signerTabs);

        return Collections.singletonList(signer);
    }

    private static java.util.List<NotaryRecipient> getNotaryRecipients() {
        NotarySeal notarySealTabs = new NotarySeal();
        notarySealTabs.setXPosition("300");
        notarySealTabs.setYPosition("235");
        notarySealTabs.setDocumentId("1");
        notarySealTabs.setPageNumber("1");

        SignHere notarySignHere = new SignHere();
        notarySignHere.setXPosition("300");
        notarySignHere.setYPosition("150");
        notarySignHere.setDocumentId("1");
        notarySignHere.setPageNumber("1");

        Tabs notaryTabs = new Tabs();
        notaryTabs.setSignHereTabs(Collections.singletonList(notarySignHere));
        notaryTabs.setNotarySealTabs(Collections.singletonList(notarySealTabs));

        NotaryRecipient notaryRecipient = new NotaryRecipient();
        notaryRecipient.setEmail("");
        notaryRecipient.setName("Notary");
        notaryRecipient.setRecipientId("1");
        notaryRecipient.setRoutingOrder("1");
        notaryRecipient.setTabs(notaryTabs);
        notaryRecipient.setNotaryType("remote");
        notaryRecipient.setNotarySourceType("thirdparty");
        notaryRecipient.setNotaryThirdPartyPartner("onenotary");

        RecipientSignatureProvider signatureProvider = new RecipientSignatureProvider();
        signatureProvider.setSealDocumentsWithTabsOnly("false");
        signatureProvider.setSignatureProviderName("ds_authority_idv");
        signatureProvider.setSignatureProviderOptions(new RecipientSignatureProviderOptions());

        notaryRecipient.setRecipientSignatureProviders(Collections.singletonList(signatureProvider));

        return Collections.singletonList(notaryRecipient);
    }
}

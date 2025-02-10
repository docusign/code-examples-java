package com.docusign.controller.notary.services;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.core.common.DocumentType;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.model.*;
import com.docusign.webforms.client.ApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public final class SendWithThirdPartyNotaryService {
    private static final String HTML_DOCUMENT_FILE_NAME = "order_form.html";

    private static final String HTML_DOCUMENT_NAME = "Order form";

    //ds-snippet-start:Notary4Step4
    public static String sendWithNotary(String signerEmail, String signerName, String accountId,
            EnvelopesApi envelopesApi, WorkArguments args)
            throws ApiException, com.docusign.esign.client.ApiException, IOException {
        EnvelopeDefinition envelopeDefinition = makeEnvelope(signerEmail, signerName, args);

        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        return envelopeSummary.getEnvelopeId();
    }
    //ds-snippet-end:Notary4Step4

    //ds-snippet-start:Notary4Step3
    private static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName, WorkArguments args)
            throws IOException {
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document set");

        envelopeDefinition.setDocuments(getDocuments(signerEmail, signerName, args));

        Recipients recipients = new Recipients();
        recipients.setSigners(getSigners(signerEmail, signerName));
        recipients.setNotaries(getNotaryRecipients());

        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setStatus("sent");

        return envelopeDefinition;
    }

    private static java.util.List<Document> getDocuments(String signerEmail, String signerName, WorkArguments args)
            throws IOException {
        byte[] htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args)
                .getBytes(StandardCharsets.UTF_8);
        Document document = EnvelopeHelpers.createDocument(htmlDoc, HTML_DOCUMENT_NAME,
                DocumentType.HTML.getDefaultFileExtention(), "1");

        return Collections.singletonList(document);
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
    //ds-snippet-end:Notary4Step3
}

package com.docusign.controller.eSignature.services;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeViewDocumentSettings;
import com.docusign.esign.model.EnvelopeViewRecipientSettings;
import com.docusign.esign.model.EnvelopeViewRequest;
import com.docusign.esign.model.EnvelopeViewSettings;
import com.docusign.esign.model.EnvelopeViewTaggerSettings;
import com.docusign.esign.model.EnvelopeViewTemplateSettings;
import com.docusign.esign.model.ViewUrl;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class EmbeddedSendingService {
    //ds-snippet-start:eSign11Step3
    public static ViewUrl createSenderView(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            String dsReturnUrl,
            String startingScreen
    ) throws ApiException {
        // Create the sender view.
        // Set the url where you want the recipient to go once they are done
        // signing should typically be a callback route somewhere in your app.
        EnvelopeViewRecipientSettings recipientSettings = new EnvelopeViewRecipientSettings();
        recipientSettings.setShowEditRecipients("false");
        recipientSettings.setShowContactsList("false");

        EnvelopeViewDocumentSettings documentSettings = new EnvelopeViewDocumentSettings();
        documentSettings.setShowEditDocuments("false");
        documentSettings.setShowEditDocumentVisibility("false");
        documentSettings.setShowEditPages("false");

        EnvelopeViewTaggerSettings taggerSettings = new EnvelopeViewTaggerSettings();
        taggerSettings.setPaletteSections("default");
        taggerSettings.setPaletteDefault("custom");

        EnvelopeViewTemplateSettings templateSettings = new EnvelopeViewTemplateSettings();
        templateSettings.setShowMatchingTemplatesPrompt("true");

        EnvelopeViewSettings viewSettings = new EnvelopeViewSettings();
        viewSettings.setStartingScreen(startingScreen);
        viewSettings.setSendButtonAction("send");
        viewSettings.setShowBackButton("false");
        viewSettings.setBackButtonAction("previousPage");
        viewSettings.setShowHeaderActions("false");
        viewSettings.setShowDiscardAction("false");
        viewSettings.setLockToken("");
        viewSettings.setRecipientSettings(recipientSettings);
        viewSettings.setDocumentSettings(documentSettings);
        viewSettings.setTaggerSettings(taggerSettings);
        viewSettings.setTemplateSettings(templateSettings);

        EnvelopeViewRequest viewRequest = new EnvelopeViewRequest();
        viewRequest.setReturnUrl(dsReturnUrl);
        viewRequest.setViewAccess("envelope");
        viewRequest.setSettings(viewSettings);

        var senderViewResponse = envelopesApi.createSenderViewWithHttpInfo(accountId, envelopeId, viewRequest);
        Map<String, List<String>> headers = senderViewResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return senderViewResponse.getData();
    }
    //ds-snippet-end:eSign11Step3

    //ds-snippet-start:eSign11Step2
    public static EnvelopeSummary createEnvelopeWithDraftStatus(
            EnvelopesApi envelopesApi,
            String signerEmail,
            String signerName,
            String ccEmail,
            String ccName,
            String status,
            WorkArguments args,
            String accountId
    ) throws IOException, ApiException {
        args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);
        EnvelopeDefinition env = SigningViaEmailService.makeEnvelope(
                signerEmail,
                signerName,
                ccEmail,
                ccName,
                status,
                args);
        var createEnvelope = envelopesApi.createEnvelopeWithHttpInfo(accountId, env, envelopesApi.new CreateEnvelopeOptions());
        Map<String, List<String>> headers = createEnvelope.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return createEnvelope.getData();
    }
    //ds-snippet-end:eSign11Step2
}

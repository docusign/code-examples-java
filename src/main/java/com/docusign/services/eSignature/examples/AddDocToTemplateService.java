package com.docusign.services.eSignature.examples;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.docusign.core.common.DocumentType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public final class AddDocToTemplateService {
    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon2.ftl";
    private static final String HTML_DOCUMENT_NAME = "Appendix 1--Sales order";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;

    public static ViewUrl addDocumentToTemplate(
            EnvelopesApi envelopesApi,
            String signerEmail,
            String signerName,
            String signerClientId,
            String ccEmail,
            String ccName,
            String templateId,
            String accountId,
            String dsReturnUrl,
            String dsPingUrl,
            WorkArguments args
    ) throws ApiException, IOException {
        // Step 2 start
        EnvelopeDefinition envelope = AddDocToTemplateService.makeEnvelope(
                signerEmail,
                signerName,
                signerClientId,
                ccEmail,
                ccName,
                templateId,
                args
        );
        // Step 2 end

        // Step 3 start
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
        // Step 3 end

        // Step 4 start
        RecipientViewRequest viewRequest = AddDocToTemplateService.makeRecipientViewRequest(
            dsReturnUrl,
            signerEmail,
            signerName,
            signerClientId,
            dsPingUrl
        );
        return envelopesApi.createRecipientView(accountId, results.getEnvelopeId(), viewRequest);
    }

    private static RecipientViewRequest makeRecipientViewRequest(
            String dsReturnUrl,
            String signerEmail,
            String signerName,
            String signerClientId,
            String dsPingUrl
    ) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        viewRequest.setReturnUrl(dsReturnUrl);
        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        String authenticationMethod = "none";
        viewRequest.setAuthenticationMethod(authenticationMethod);
        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(signerClientId);

        // DocuSign recommends that you redirect to DocuSign for the
        // embedded signing. There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        String pingFrequency = "600";
        viewRequest.setPingFrequency(pingFrequency);
        viewRequest.setPingUrl(dsPingUrl);

        return viewRequest;
    }

    // The envelope request object uses Composite Template to include in the envelope:
    // 1. A template stored on the DocuSign service
    // 2. An additional document which is a custom HTML source document
    private static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String signerClientId,
            String ccEmail,
            String ccName,
            String templateId,
            WorkArguments args
    ) throws IOException {
        // Create a signer and cc recipients for the signer role of the server template
        CarbonCopy cc1 = new CarbonCopy();
        cc1.setEmail(ccEmail);
        cc1.setName(ccName);
        cc1.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc1.setRecipientId("2");

        // create a composite template for the Server Template
        CompositeTemplate compTemplate1 = new CompositeTemplate();
        compTemplate1.setCompositeTemplateId("1");
        ServerTemplate serverTemplates = new ServerTemplate();
        serverTemplates.setSequence("1");
        serverTemplates.setTemplateId(templateId);
        compTemplate1.setServerTemplates(Collections.singletonList(serverTemplates));

        // Add the roles via an inlineTemplate
        InlineTemplate inlineTemplate = new InlineTemplate();
        inlineTemplate.setSequence("2");
        inlineTemplate.setRecipients(EnvelopeHelpers.createRecipients(
                createSigner(
                     signerEmail,
                     signerName,
                     signerClientId
                ),
                cc1));
        compTemplate1.setInlineTemplates(Collections.singletonList(inlineTemplate));

        // The signer recipient for the added document with a tab definition:
        Tabs signer1Tabs = EnvelopeHelpers.createSingleSignerTab(
                "**signature_1**",
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X);
        Signer signer1AddedDoc = createSigner(
            signerEmail,
            signerName,
            signerClientId
        );
        signer1AddedDoc.setTabs(signer1Tabs);

        // create the HTML document
        byte[] htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args);

        // create a composite template for the added document and add the recipients via an inlineTemplate
        CompositeTemplate compTemplate2 = new CompositeTemplate();
        compTemplate2.setCompositeTemplateId("2");
        InlineTemplate inlineTemplate2 = new InlineTemplate();
        inlineTemplate2.setSequence("1");
        inlineTemplate2.setRecipients(EnvelopeHelpers.createRecipients(signer1AddedDoc, cc1));
        compTemplate2.setInlineTemplates(Collections.singletonList(inlineTemplate2));
        compTemplate2.setDocument(EnvelopeHelpers.createDocument(htmlDoc, HTML_DOCUMENT_NAME,
                DocumentType.HTML.getDefaultFileExtention(), "1"));

        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        env.setCompositeTemplates(Arrays.asList(compTemplate1, compTemplate2));

        return env;
    }

    // Adding clientUserId transforms the template recipient into an embedded recipient
    private static Signer createSigner(
            String signerEmail,
            String signerName,
            String signerClientId
    ) {
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId("1");
        signer.setClientUserId(signerClientId);
        return signer;
    }
}

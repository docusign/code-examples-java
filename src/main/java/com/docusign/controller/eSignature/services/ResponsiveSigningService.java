package com.docusign.controller.eSignature.services;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public final class ResponsiveSigningService {
    //ds-snippet-start:eSign38Step2
    private static final String TRUE = "true";

    private static final String FALSE = "false";

    private static final int L1_PRICE = 5;

    private static final int L2_PRICE = 150;

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    private static final String DEFAULT_FONT = "helvetica";

    private static final String DEFAULT_FONT_SIZE = "size11";

    private static final String ANCHOR_UNITS = "pixels";

    private static final String HTML_DOCUMENT_FILE_NAME = "templates/order-form.ftl";

    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String signerClientId,
            String ccEmail,
            String ccName,
            WorkArguments args
    ) throws IOException {
        String htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args);
        htmlDoc = htmlDoc
                .replace("/l1q/", " <input data-ds-type=\"number\" name=\"l1q\" />")
                .replace("/l2q/", " <input data-ds-type=\"number\" name=\"l2q\"/>");;

        Signer signer = new Signer()
                .email(signerEmail)
                .name(signerName)
                .clientUserId(signerClientId)
                .recipientId("1")
                .routingOrder("1")
                .roleName("Signer");

        CarbonCopy cc = new CarbonCopy()
                .email(ccEmail)
                .name(ccName)
                .recipientId("2")
                .routingOrder("1");

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Example Signing Document");
        envelope.setDocuments(Collections.singletonList(
                new Document()
                        .name("Lorem Ipsum")
                        .documentId("1")
                        .htmlDefinition(new DocumentHtmlDefinition().source(htmlDoc))));
        signer.setTabs(createTabs());
        envelope.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelope;
    }

    private static Tabs createTabs() {
        FormulaTab formulaForFirstNumber = createFormulaTab("/l1e/", "l1e", L1_PRICE, "l1q");
        FormulaTab formulaForSecondNumber = createFormulaTab("/l2e/", "l2e", L2_PRICE, "l2q");
        FormulaTab finalFormula = new FormulaTab()
                .font(DEFAULT_FONT)
                .bold(TRUE)
                .fontSize(DEFAULT_FONT_SIZE)
                .anchorString("/l3t/")
                .anchorYOffset("-8")
                .anchorUnits(ANCHOR_UNITS)
                .anchorXOffset("105")
                .tabLabel("l3t")
                .formula("[l1e] + [l2e]")
                .roundDecimalPlaces("0")
                .required(TRUE)
                .locked(TRUE)
                .disableAutoSize(FALSE);

        Tabs signerTabs = EnvelopeHelpers.createSingleSignerTab("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        signerTabs.formulaTabs(Arrays.asList(formulaForFirstNumber, formulaForSecondNumber, finalFormula));

        return signerTabs;
    }

    private static FormulaTab createFormulaTab(String anchor, String label, int price, String numerical) {
        return new FormulaTab()
                .font(DEFAULT_FONT)
                .fontSize(DEFAULT_FONT_SIZE)
                .anchorString(anchor)
                .anchorYOffset("-8")
                .anchorUnits(ANCHOR_UNITS)
                .anchorXOffset("105")
                .tabLabel(label)
                .formula(String.format("[%s] * {%d}", numerical, price))
                .roundDecimalPlaces("0")
                .required(TRUE)
                .locked(TRUE)
                .disableAutoSize(FALSE);
    }

    public static ViewUrl responsiveSigning(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            RecipientViewRequest viewRequest
    ) throws ApiException {
        return envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
    }

    public static RecipientViewRequest makeRecipientViewRequest(
            String signerEmail,
            String signerName,
            DSConfiguration config,
            String clientUserId
    ) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        String stateValue = "?state=123";
        viewRequest.setReturnUrl(config.getDsReturnUrl() + stateValue);

        String authenticationMethod = "none";
        viewRequest.setAuthenticationMethod(authenticationMethod);

        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);

        String pingFrequency = "600";
        viewRequest.setPingFrequency(pingFrequency);
        viewRequest.setPingUrl(config.getDsPingUrl());

        return viewRequest;
    }
    //ds-snippet-end:eSign38Step2
}

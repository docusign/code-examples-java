package com.docusign.controller.eSignature.services;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.core.common.DocumentType;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public final class CollectPaymentService {
    private static final String HTML_DOCUMENT_FILE_NAME = "templates/order-form.ftl";

    private static final String HTML_DOCUMENT_NAME = "Order form";

    // Order form constants
    private static final String TRUE = "true";

    private static final String FALSE = "false";

    private static final int L1_PRICE = 5;

    private static final int L2_PRICE = 150;

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    private static final String DEFAULT_FONT = "helvetica";

    private static final String DEFAULT_FONT_SIZE = "size11";

    private static final String ANCHOR_UNITS = "pixels";

    //ds-snippet-start:eSign14Step4
    public static EnvelopeSummary collectPayment(
            EnvelopesApi envelopesApi,
            EnvelopeDefinition envelope,
            String accountId
    ) throws ApiException {
        // Call Envelopes::create API method
        return envelopesApi.createEnvelope(accountId, envelope);
    }
    //ds-snippet-end:eSign14Step4
    // document 1 (html) has multiple tags:
    // /l1q/ and /l2q/ -- quantities: drop down
    // /l1e/ and /l2e/ -- extended: payment lines
    // /l3t/ -- total -- formula
    //
    // The envelope has two recipients.
    // recipient 1 - signer
    // recipient 2 - cc
    // The envelope will be sent first to the signer.
    // After it is signed, a copy is sent to the cc person.
    //ds-snippet-start:eSign14Step3

    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String ccName,
            String ccEmail,
            String gatewayAccountId,
            String gatewayName,
            String gatewayDisplayName,
            WorkArguments args
    ) throws IOException {
        byte[] htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args)
                .getBytes(StandardCharsets.UTF_8);

        // create a signer recipient to sign the document, identified by name and email
        // routingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.
        Signer signer = new Signer()
                .email(signerEmail)
                .name(signerName)
                .recipientId("1")
                .routingOrder("1");
        signer.setTabs(createTabs(gatewayAccountId, gatewayName, gatewayDisplayName));

        // create a cc recipient to receive a copy of the documents, identified by name and email
        CarbonCopy cc = new CarbonCopy()
                .email(ccEmail)
                .name(ccName)
                .routingOrder("2")
                .recipientId("2");

        // Create the envelope definition
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please complete your order");
        envelope.setDocuments(Collections.singletonList(EnvelopeHelpers.createDocument(htmlDoc,
                HTML_DOCUMENT_NAME, DocumentType.HTML.getDefaultFileExtention(), "1")));
        envelope.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelope;
    }

    // creates tabs per signer
    private static Tabs createTabs(String gatewayAccountId, String gatewayName, String gatewayDisplayName) {
        java.util.List<ListItem> listItems = java.util.List.of(
                new ListItem().text("none").value("0"),
                new ListItem().text("1").value("1"),
                new ListItem().text("2").value("2"),
                new ListItem().text("3").value("3"),
                new ListItem().text("4").value("4"),
                new ListItem().text("5").value("5"),
                new ListItem().text("6").value("6"),
                new ListItem().text("7").value("7"),
                new ListItem().text("8").value("8"),
                new ListItem().text("9").value("9"),
                new ListItem().text("10").value("10"));
        List listl1q = createList("/l1q/", "l1q", listItems);
        List listl2q = createList("/l2q/", "l2q", listItems);

        // Create two formula tabs for the extended price on the line items and formula for the total
        FormulaTab formulal1e = createFormulaTab("/l1e/", "l1e", L1_PRICE);
        FormulaTab formulal2e = createFormulaTab("/l2e/", "l2e", L2_PRICE);
        FormulaTab formulal3t = new FormulaTab()
                .font(DEFAULT_FONT)
                .bold(TRUE)
                .fontSize("size12")
                .anchorString("/l3t/")
                .anchorYOffset("-8")
                .anchorUnits(ANCHOR_UNITS)
                .anchorXOffset("50")
                .tabLabel("l3t")
                .formula("[l1e] + [l2e]")
                .roundDecimalPlaces("0")
                .required(TRUE)
                .locked(TRUE)
                .disableAutoSize(FALSE);
        PaymentLineItem paymentLineIteml1 = new PaymentLineItem()
                .name("Harmonica")
                .description(String.format("${%d} each", L1_PRICE))
                .amountReference("l1e");
        PaymentLineItem paymentLineIteml2 = new PaymentLineItem()
                .name("Xylophone")
                .description(String.format("${%d} each", L2_PRICE))
                .amountReference("l2e");
        PaymentDetails paymentDetails = new PaymentDetails()
                .gatewayAccountId(gatewayAccountId)
                .currencyCode("USD")
                .gatewayName(gatewayName)
                .gatewayDisplayName(gatewayDisplayName)
                .lineItems(Arrays.asList(paymentLineIteml1, paymentLineIteml2));
        // Hidden formula for the payment itself
        FormulaTab formulaPayment = new FormulaTab()
                .tabLabel("payment")
                .formula("([l1e] + [l2e]) * {100}")
                .roundDecimalPlaces("0")
                .paymentDetails(paymentDetails)
                .hidden(TRUE)
                .required(TRUE)
                .locked(TRUE)
                .documentId("1")
                .pageNumber("1")
                .xPosition("0")
                .yPosition("0");

        Tabs signerTabs = EnvelopeHelpers.createSingleSignerTab("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        signerTabs.listTabs(Arrays.asList(listl1q, listl2q));
        signerTabs.formulaTabs(Arrays.asList(formulal1e, formulal2e, formulal3t, formulaPayment));
        return signerTabs;
    }

    private static List createList(String anchor, String label, java.util.List<ListItem> items) {
        return new List()
                .font(DEFAULT_FONT)
                .fontSize(DEFAULT_FONT_SIZE)
                .anchorString(anchor)
                .anchorYOffset("-10")
                .anchorUnits(ANCHOR_UNITS)
                .anchorXOffset("0")
                .listItems(items)
                .required(TRUE)
                .tabLabel(label);
    }

    private static FormulaTab createFormulaTab(String anchor, String label, int price) {
        return new FormulaTab()
                .font(DEFAULT_FONT)
                .fontSize(DEFAULT_FONT_SIZE)
                .anchorString(anchor)
                .anchorYOffset("-8")
                .anchorUnits(ANCHOR_UNITS)
                .anchorXOffset("105")
                .tabLabel(label)
                .formula(String.format("[l1q] * {%d}", price))
                .roundDecimalPlaces("0")
                .required(TRUE)
                .locked(TRUE)
                .disableAutoSize(FALSE);
    }
    //ds-snippet-end:eSign14Step3
}

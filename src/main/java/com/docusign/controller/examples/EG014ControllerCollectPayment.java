package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.sun.jersey.core.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/eg014")
public class EG014ControllerCollectPayment extends EGController {
    @Override
    protected void addSpecialAttributes(ModelMap model) {
        model.addAttribute("gatewayOk", null != config.gatewayAccountId);
    }

    @Override
    protected String getEgName() {
        return "eg014";
    }

    @Override
    protected String getTitle() {
        return "Envelope sent";
    }

    @Override
    protected String getResponseTitle() {
        return null;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, String accessToken, String basePath) throws ApiException, IOException {
        // Data for this method
        // accessToken  (argument)
        // basePath     (argument)
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String ccEmail = args.getCcEmail();
        String ccName = args.getCcName();
        String status = args.getStatus();
        String accountId = args.getAccountId();


        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 1. Make the envelope request body
        EnvelopeDefinition envelope = makeEnvelope(signerEmail, signerName,
                ccEmail, ccName, status);

        // Step 2. call Envelopes::create API method
        // Exceptions will be caught by the calling function
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
        // process results
        String envelopeId = results.getEnvelopeId();
        System.out.println("Envelope was created.EnvelopeId " + envelopeId);
        this.setMessage("The envelope has been created and sent!<br/>Envelope ID " + results.getEnvelopeId() + ".");
        return null;
    }

    private EnvelopeDefinition makeEnvelope(String signerEmail, String signerName,
                                            String ccEmail, String ccName, String status) throws IOException {
        // Data for this method
        // signerEmail  (argument)
        // signerName   (argument)
        // ccEmail      (argument)
        // ccName       (argument)
        // status       (argument)
        // payment gateway configuration settings:
        //   config.gatewayAccountId
        //   config.gatewayName
        //   config.gatewayDisplayName


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

        ///////////////////////////////////////////////////////////////////
        //                                                               //
        // NOTA BENA: This method programmatically constructs the        //
        //            order form. For many use cases, it would be        //
        //            better to create the order form as a template      //
        //            using the DocuSign web tool as a WYSIWYG           //
        //            form designer.                                     //
        //                                                               //
        ///////////////////////////////////////////////////////////////////

        // Order form constants
        String l1Name = "Harmonica";
        int l1Price = 5;
        String l1Description = "${"+l1Price+"} each"
                , l2Name = "Xylophone";
        int l2Price = 150;
        String l2Description = "${"+l2Price+"} each";
        int currencyMultiplier = 100;

        // read file from a local directory
        // The read could raise an exception if the file is not available!
        String doc1HTML1 = new String(readFile("order_form.html"));
        //string doc1HTML1 = fs.readFileSync(path.resolve(demoDocsPath, doc1File),
        //        { encoding: 'utf8'});

        // Substitute values into the HTML
        // Substitute for: {signerName}, {signerEmail}, {ccName}, {ccEmail}
        String doc1HTML2 = doc1HTML1.replace("{signerName}", signerName)
                .replace("{signerEmail}", signerEmail)
                .replace("{ccName}", ccName)
                .replace("{ccEmail}", ccEmail);


        // create the envelope definition
        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setEmailSubject("Please complete your order");

        // add the documents
        Document doc1 = new Document();

        String doc1b64 = new String(Base64.encode(doc1HTML2));

        doc1.setDocumentBase64(doc1b64);
        doc1.setName("Order form"); // can be different from actual file name
        doc1.setFileExtension("html"); // Source data format. Signed docs are always pdf.
        doc1.setDocumentId("1"); // a label used to reference the doc
        env.setDocuments(Arrays.asList(doc1));

        // create a signer recipient to sign the document, identified by name and email
        // We're setting the parameters via the object creation
        Signer signer1 = new Signer()
                .email(signerEmail)
                .name(signerName)
                .recipientId("1")
                .routingOrder("1");
        // routingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.

        // create a cc recipient to receive a copy of the documents, identified by name and email
        // We're setting the parameters via setters
        CarbonCopy cc1 = new CarbonCopy()
                .email(ccEmail)
                .name(ccName)
                .routingOrder("2")
                .recipientId("2");
        // Create signHere fields (also known as tabs) on the documents,
        // We're using anchor (autoPlace) positioning
        SignHere signHere1 = new SignHere()
                .anchorString("/sn1/")
                .anchorYOffset("10")
                .anchorUnits("pixels")
                .anchorXOffset("20");

        ListItem listItem0 = new ListItem()
                .text("none")
                .value("0")
            , listItem1 = new ListItem()
                .text("1")
                .value("1")
           , listItem2 = new ListItem()
                .text("2")
                .value("2")
           , listItem3 = new ListItem()
                .text("3")
                .value("3")
           , listItem4 = new ListItem()
                .text("4")
                .value("4")
           , listItem5 = new ListItem()
                .text("5")
                .value("5")
           , listItem6 = new ListItem()
                .text("6")
                .value("6")
           , listItem7 = new ListItem()
                .text("7")
                .value("7")
           , listItem8 = new ListItem()
                .text("8")
                .value("8")
           , listItem9 = new ListItem()
                .text("9")
                .value("9")
           , listItem10 = new ListItem()
                .text("10")
                .value("10");

        List listl1q = new List()
            .font("helvetica")
                .fontSize("size11")
                .anchorString( "/l1q/")
                .anchorYOffset("-10")
                .anchorUnits("pixels")
                .anchorXOffset("0")
                .listItems(Arrays.asList(listItem0, listItem1, listItem2,
                        listItem3, listItem4, listItem5, listItem6,
                        listItem7, listItem8, listItem9, listItem10 ))
                .required("true")
                .tabLabel("l1q")
        ,
        listl2q = new List()
                .font("helvetica")
                .fontSize("size11")
                .anchorString( "/l2q/")
                .anchorYOffset("-10")
                .anchorUnits("pixels")
                .anchorXOffset("0")
                .listItems(Arrays.asList(listItem0, listItem1, listItem2,
                        listItem3, listItem4, listItem5, listItem6,
                        listItem7, listItem8, listItem9, listItem10 ))
                .required("true")
                .tabLabel("l2q");

        // create two formula tabs for the extended price on the line items
        FormulaTab formulal1e = new FormulaTab()
                .font("helvetica")
                .fontSize("size11")
                .anchorString("/l1e/")
                .anchorYOffset("-8")
                .anchorUnits("pixels")
                .anchorXOffset("105")
                .tabLabel("l1e")
                .formula("[l1q] * {"+l1Price+"}")
                .roundDecimalPlaces("0")
                .required("true")
                .locked("true")
                .disableAutoSize("false"),
        formulal2e = new FormulaTab()
                .font("helvetica")
                .fontSize("size11")
                .anchorString("/l2e/")
                .anchorYOffset("-8")
                .anchorUnits("pixels")
                .anchorXOffset("105")
                .tabLabel("l2e")
                .formula("[l2q] * {"+l2Price+"}")
                .roundDecimalPlaces("0")
                .required("true")
                .locked("true")
                .disableAutoSize("false"),
        // Formula for the total
      formulal3t = new FormulaTab()
              .font("helvetica")
              .bold("true")
              .fontSize("size12")
              .anchorString("/l3t/")
              .anchorYOffset("-8")
              .anchorUnits("pixels")
              .anchorXOffset("50")
              .tabLabel("l3t")
              .formula("[l1e] + [l2e]")
              .roundDecimalPlaces("0")
              .required("true")
              .locked("true")
              .disableAutoSize("false");
        // Payment line items
        PaymentLineItem paymentLineIteml1 = new PaymentLineItem()
                .name(l1Name)
                .description(l1Description)
                .amountReference("l1e"),
        paymentLineIteml2 = new PaymentLineItem()
            .name(l2Name)
                .description(l2Description)
                .amountReference("l2e");
        PaymentDetails paymentDetails = new PaymentDetails()
            .gatewayAccountId(config.gatewayAccountId)
                .currencyCode("USD")
                .gatewayName(config.gatewayName)
                .gatewayDisplayName(config.gatewayDisplayName)
                .lineItems(Arrays.asList(paymentLineIteml1, paymentLineIteml2));
        // Hidden formula for the payment itself
        FormulaTab formulaPayment = new FormulaTab()
            .tabLabel("payment")
                .formula("([l1e] + [l2e]) * {"+currencyMultiplier+"}")
                .roundDecimalPlaces("0")
                .paymentDetails(paymentDetails)
                .hidden("true")
                .required("true")
                .locked("true")
                .documentId("1")
                .pageNumber("1")
                .xPosition("0")
                .yPosition("0");

        // Tabs are set per recipient / signer
        Tabs signer1Tabs = new Tabs()
            .signHereTabs(Arrays.asList(signHere1))
                .listTabs(Arrays.asList(listl1q, listl2q))
                .formulaTabs(Arrays.asList(formulal1e, formulal2e, formulal3t, formulaPayment));
        signer1.setTabs(signer1Tabs);

        // Add the recipients to the envelope object
        Recipients recipients = new Recipients()
                .signers(Arrays.asList(signer1))
                .carbonCopies(Arrays.asList(cc1));

        env.setRecipients(recipients);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        env.setStatus("sent");

        return env;
    }
    // ***DS.snippet.0.end
}

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
@RequestMapping("/eg002")
public class EG002ControllerSigningViaEmail extends EGController {

    @Override
    protected void addSpecialAttributes(ModelMap model) {
    }

    @Override
    protected String getEgName() {
        return "eg002";
    }

    @Override
    protected String getTitle() {
        return "Signing request by email";
    }

    @Override
    protected String getResponseTitle() {
        return "Envelope sent";
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            String accessToken, String basePath) throws ApiException, IOException {
        // Data for this method
        // accessToken  (argument)
        // basePath     (argument)
        String accountId = args.getAccountId();


        // Set status for the makeEnvelope method
        if (!"created".equalsIgnoreCase(args.getStatus())) {
            args.setStatus("sent");
        }
        EnvelopeDefinition env = makeEnvelope(args);
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, env);

        // process results
        args.setEnvelopeId(results.getEnvelopeId());
        session.setAttribute("envelopeId", results.getEnvelopeId());
        setMessage("The envelope has been created and sent!<br />Envelope ID " + args.getEnvelopeId() + ".");
        return results;
    }

    private EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {
        // Data for this method
        // config.docDocx Name of the docx demo document
        // config.docPdf  Name of the pdf demo document
        String signerEmail = args.getSignerEmail();
        String signerName = args.getSignerName();
        String ccEmail = args.getCcEmail();
        String ccName = args.getCcName();
        String status = args.getStatus();


        // document 1 (html) has tag **signature_1**
        // document 2 (docx) has tag /sn1/
        // document 3 (pdf) has tag /sn1/
        //
        // The envelope has two recipients.
        // recipient 1 - signer
        // recipient 2 - cc
        // The envelope will be sent first to the signer.
        // After it is signed, a copy is sent to the cc person.
        // read files from a local directory
        // The reads could raise an exception if the file is not available!
        String doc2DocxBytes = new String(Base64.encode(readFile(config.docDocx)));
        String doc3PdfBytes = new String(Base64.encode(readFile(config.docPdf)));
        // create the envelope definition
        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setEmailSubject("Please sign this document set");
        Document doc1 = new Document();
        String b64 = new String(Base64.encode(document1(args).getBytes()));
        doc1.setDocumentBase64(b64);
        doc1.setName("Order acknowledgement"); // can be different from actual file name
        doc1.setFileExtension("html"); // Source data format. Signed docs are always pdf.
        doc1.setDocumentId("1"); // a label used to reference the doc
        Document doc2 = new Document();
        doc2.setDocumentBase64(doc2DocxBytes);
        doc2.setName("Battle Plan"); // can be different from actual file name
        doc2.setFileExtension("docx");
        doc2.setDocumentId("2");
        Document doc3 = new Document();
        doc3.setDocumentBase64(doc3PdfBytes);
        doc3.setName("Lorem Ipsum"); // can be different from actual file name
        doc3.setFileExtension("pdf");
        doc3.setDocumentId("3");

        // The order in the docs array determines the order in the envelope
        env.setDocuments(Arrays.asList(doc1, doc2, doc3));

        // create a signer recipient to sign the document, identified by name and email
        // We're setting the parameters via the object creation
        Signer signer1 = new Signer();
        signer1.setEmail(signerEmail);
        signer1.setName(signerName);
        signer1.setRecipientId("1");
        signer1.setRoutingOrder("1");

        // routingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.

        // create a cc recipient to receive a copy of the documents, identified by name and email
        // We're setting the parameters via setters
        CarbonCopy cc1 = new CarbonCopy();
        cc1.setEmail(ccEmail);
        cc1.setName(ccName);
        cc1.setRecipientId("2");
        cc1.setRoutingOrder("2");
        // Create signHere fields (also known as tabs) on the documents,
        // We're using anchor (autoPlace) positioning
        //
        // The DocuSign platform searches throughout your envelope's
        // documents for matching anchor strings. So the
        // signHere2 tab will be used in both document 2 and 3 since they
        // use the same anchor string for their "signer 1" tabs.
        SignHere signHere1 = new SignHere();
        signHere1.setAnchorString("**signature_1**");
        signHere1.setAnchorUnits("pixels");
        signHere1.setAnchorYOffset("10");
        signHere1.setAnchorXOffset("20");
        SignHere signHere2 = new SignHere();
        signHere2.setAnchorString("/sn1/");
        signHere2.setAnchorUnits("pixels");
        signHere2.setAnchorYOffset("10");
        signHere2.setAnchorXOffset("20");

        // Tabs are set per recipient / signer
        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setSignHereTabs(Arrays.asList(signHere1, signHere2));
        signer1.setTabs(signer1Tabs);

        // Add the recipients to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer1));
        recipients.setCarbonCopies(Arrays.asList(cc1));
        env.setRecipients(recipients);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        env.setStatus(status);
        return env;
    }

    private String document1(WorkArguments args) {
        // Data for this method
        String signerEmail = args.getSignerEmail();
        String signerName = args.getSignerName();
        String ccEmail = args.getCcEmail();
        String ccName = args.getCcName();


        return " <!DOCTYPE html>\n" +
                "    <html>\n" +
                "        <head>\n" +
                "          <meta charset=\"UTF-8\">\n" +
                "        </head>\n" +
                "        <body style=\"font-family:sans-serif;margin-left:2em;\">\n" +
                "        <h1 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif;\n" +
                "            color: darkblue;margin-bottom: 0;\">World Wide Corp</h1>\n" +
                "        <h2 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif;\n" +
                "          margin-top: 0px;margin-bottom: 3.5em;font-size: 1em;\n" +
                "          color: darkblue;\">Order Processing Division</h2>\n" +
                "        <h4>Ordered by " + signerName + "</h4>\n" +
                "        <p style=\"margin-top:0em; margin-bottom:0em;\">Email: " + signerEmail + "</p>\n" +
                "        <p style=\"margin-top:0em; margin-bottom:0em;\">Copy to: " + ccName + ", " + ccEmail + "</p>\n" +
                "        <p style=\"margin-top:3em;\">\n" +
                "  Candy bonbon pastry jujubes lollipop wafer biscuit biscuit. Topping brownie sesame snaps sweet roll pie. Croissant danish biscuit soufflé caramels jujubes jelly. Dragée danish caramels lemon drops dragée. Gummi bears cupcake biscuit tiramisu sugar plum pastry. Dragée gummies applicake pudding liquorice. Donut jujubes oat cake jelly-o. Dessert bear claw chocolate cake gummies lollipop sugar plum ice cream gummies cheesecake.\n" +
                "        </p>\n" +
                "        <!-- Note the anchor tag for the signature field is in white. -->\n" +
                "        <h3 style=\"margin-top:3em;\">Agreed: <span style=\"color:white;\">**signature_1**/</span></h3>\n" +
                "        </body>\n" +
                "    </html>";
    }
}

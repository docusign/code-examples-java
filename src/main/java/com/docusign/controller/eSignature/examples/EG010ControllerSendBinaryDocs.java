package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.common.DocumentType;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;

import lombok.Value;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * Send an envelope using binary document transfer.<br />
 * The envelope includes a pdf, doc, and HTML document. Multipart data transfer
 * is used to send the documents in binary format to DocuSign.
 */
@Controller
@RequestMapping("/eg010")
public class EG010ControllerSendBinaryDocs extends AbstractEsignatureController {

    private static final String HYPHENS = "--";
    private static final String LINE_DELIMITER = "\r\n";
    private static final String BOUNDARY_DELIMITER = "multipartboundary_multipartboundary";

    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon.ftl";
    private static final String HTML_DOCUMENT_NAME = "Order acknowledgement";
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";
    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;

    private final Session session;
    private final User user;


    @Autowired
    public EG010ControllerSendBinaryDocs(DSConfiguration config, Session session, User user) {
        super(config, "eg010", "Send envelope with multipart mime");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws IOException {
        // Step 1. Gather documents and their headers
        List<DocumentInfo> documents = List.of(
                new DocumentInfo(HTML_DOCUMENT_NAME, "1", DocumentType.HTML,
                        EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args)),
                new DocumentInfo(DOCX_DOCUMENT_NAME, "2", DocumentType.DOCX,
                        EnvelopeHelpers.readFile(DOCX_DOCUMENT_FILE_NAME)),
                new DocumentInfo(PDF_DOCUMENT_NAME, "3", DocumentType.PDF,
                        EnvelopeHelpers.readFile(PDF_DOCUMENT_FILE_NAME))
                );

        // Step 2. Make the envelope JSON request body
        JSONObject envelopeJSON = makeEnvelopeJSON(args, documents);

        // Step 3. Create the multipart body
        URL uri = new URL(String.format("%s/v2.1/accounts/%s/envelopes", session.getBasePath(), session.getAccountId()));
        String contentType = String.join("",MediaType.MULTIPART_FORM_DATA, "; boundary=", BOUNDARY_DELIMITER);
        HttpsURLConnection connection = (HttpsURLConnection) uri.openConnection();
        connection.setRequestMethod(HttpMethod.POST);
        connection.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + user.getAccessToken());
        connection.setDoOutput(true);

        // See https://developers.docusign.com/esign-rest-api/guides/requests-and-responses
        DataOutputStream buffer = new DataOutputStream(connection.getOutputStream());
        writeBoundaryHeader(buffer, MediaType.APPLICATION_JSON, "form-data");
        buffer.writeBytes(envelopeJSON.toString(DoneExample.JSON_INDENT_FACTOR));

        for (DocumentInfo docInfo : documents) {
            String content = String.format("file; filename=\"%s\";documentid=%s", docInfo.getName(), docInfo.getId());
            buffer.writeBytes(LINE_DELIMITER);
            writeBoundaryHeader(buffer, docInfo.getDocType().getMime(), content);
            buffer.write(docInfo.getData());
        }

        writeClosingBoundary(buffer);

        int responseCode = connection.getResponseCode();
        if (responseCode < HttpURLConnection.HTTP_OK || responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) {
            String error = StreamUtils.copyToString(connection.getErrorStream(), StandardCharsets.UTF_8);
            throw new ExampleException(error, null);
        }

        String responseString = StreamUtils.copyToString(connection.getInputStream(), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(responseString);
        String envelopeId = obj.getString("envelopeId");
        session.setEnvelopeId(envelopeId);

        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br/>Envelope ID " + envelopeId + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    private static void writeBoundaryHeader(DataOutputStream buffer,
            String contentType, String contentDisposition) throws IOException {
        buffer.writeBytes(HYPHENS);
        buffer.writeBytes(BOUNDARY_DELIMITER);
        buffer.writeBytes(LINE_DELIMITER);
        buffer.writeBytes(String.join(": ", HttpHeaders.CONTENT_TYPE, contentType));
        buffer.writeBytes(LINE_DELIMITER);
        buffer.writeBytes(String.join(": ", HttpHeaders.CONTENT_DISPOSITION, contentDisposition));
        buffer.writeBytes(LINE_DELIMITER);
        buffer.writeBytes(LINE_DELIMITER);
    }

    private static void writeClosingBoundary(DataOutputStream buffer) throws IOException {
        buffer.writeBytes(LINE_DELIMITER);
        buffer.writeBytes(HYPHENS);
        buffer.writeBytes(BOUNDARY_DELIMITER);
        buffer.writeBytes(HYPHENS);
        buffer.writeBytes(LINE_DELIMITER);
        buffer.flush();
    }

    // document 1 (html) has tag **signature_1**
    // document 2 (docx) has tag /sn1/
    // document 3 (pdf) has tag /sn1/
    //
    // The envelope has two recipients.
    // recipient 1 - signer
    // recipient 2 - cc
    // The envelope will be sent first to the signer.
    // After it is signed, a copy is sent to the cc person.
    private static JSONObject makeEnvelopeJSON(WorkArguments args, List<DocumentInfo> documents) {
        // The DocuSign platform searches throughout your envelope's documents for
        // matching anchor strings. So the signHere2 tab will be used in both document
        // 2 and 3 since they use the same anchor string for their "signer 1" tabs.
        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("**signature_1**", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X),
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        // create a signer recipient to sign the document, identified by name and email
        // RoutingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.
        Signer signer = new Signer();
        signer.setEmail(args.getSignerEmail());
        signer.setName(args.getSignerName());
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);

        // create a cc recipient to receive a copy of the documents, identified by name and email
        CarbonCopy cc = new CarbonCopy();
        cc.setEmail(args.getCcEmail());
        cc.setName(args.getCcName());
        cc.setRecipientId("2");
        cc.setRoutingOrder("2");

        // The order in the documents array determines the order in the envelope
        JSONArray jsonDocuments = new JSONArray();
        for (DocumentInfo docInfo : documents) {
            JSONObject jsonDoc = new JSONObject();
            jsonDoc.put("name", docInfo.getName());
            jsonDoc.put("fileExtension", docInfo.getDocType().getDefaultFileExtention());
            jsonDoc.put("documentId", docInfo.getId());
            jsonDocuments.put(jsonDoc);
        }

        // Create the envelope definition. Request that the envelope be sent by
        // setting |status| to "sent". To request that the envelope be created
        // as a draft, set to "created"
        JSONObject envelopeJSON = new JSONObject();
        envelopeJSON.put("emailSubject", "Please sign this document set");
        envelopeJSON.put("documents", jsonDocuments);
        envelopeJSON.put("recipients", new JSONObject(EnvelopeHelpers.createRecipients(signer, cc)));
        envelopeJSON.put("status", EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeJSON;
    }

    @Value
    private static class DocumentInfo {
        private String name;
        private String id;
        private DocumentType docType;
        private byte[] data;
    }
    // ***DS.snippet.0.end
}

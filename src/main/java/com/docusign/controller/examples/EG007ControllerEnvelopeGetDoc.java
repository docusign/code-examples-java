package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.model.OptionItem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/eg007")
public class EG007ControllerEnvelopeGetDoc extends EGController {
    @Autowired
    HttpSession session;

    @Override
    protected void addSpecialAttributes(ModelMap model) {
        model.addAttribute("envelopeOk", session.getAttribute("envelopeId") != null);
        boolean documentsOk = session.getAttribute("envelopeDocuments") != null;
        model.addAttribute("documentsOk", documentsOk);
        if (documentsOk == false) {
            return;
        }
        JSONObject envelopeDocuments = (JSONObject) session.getAttribute("envelopeDocuments");
        JSONArray documents = (JSONArray) envelopeDocuments.get("documents");

        ArrayList<OptionItem> documentOptions = new ArrayList<>();
        for (int i = 0; i < documents.length(); i++) {
            OptionItem doc = new OptionItem();

            doc.setText(documents.getJSONObject(i).getString("name"));
            doc.setDocumentId(documents.getJSONObject(i).getString("documentId"));
            documentOptions.add(doc);
        }

        model.addAttribute("documentOptions", documentOptions);
    }

    @Override
    protected String getEgName() {
        return "eg007";
    }

    @Override
    protected String getTitle() {
        return "Download a document";
    }

    @Override
    protected String getResponseTitle() {
        return null;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            String accessToken, String basePath) throws ApiException {
        // Data for this method
        // accessToken    (argument)
        // basePath       (argument)
        String accountId = args.getAccountId();
        String envelopeId = args.getEnvelopeId();
        String documentId = args.getDocumentId();
        JSONObject envelopeDocuments = args.getEnvelopeDocuments(); // stored by EG006ControllerEnvelopeDocs


        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 1. EnvelopeDocuments::get.
        // Exceptions will be caught by the calling function
        byte[] results = envelopesApi.getDocument(accountId, envelopeId, documentId);

        // Step 2. process results
        JSONArray documents = envelopeDocuments.getJSONArray("documents");
        JSONObject docItem = find(documents, documentId);

        String docName = docItem.getString("name");
        boolean hasPDFsuffix = docName.toUpperCase().endsWith(".PDF");
        boolean pdfFile = hasPDFsuffix;
        // Add .pdf if it's a content or summary doc and doesn't already end in .pdf
        String docType = docItem.getString("type");
        if (("content".equals(docType) || "summary".equals(docType)) && !hasPDFsuffix) {
            docName += ".pdf";
            pdfFile = true;
        }
        // Add .zip as appropriate
        if ("zip".equals(docType)) {
            docName += ".zip";
        }
        // Return the file information
        // See https://stackoverflow.com/a/30625085/64904
        String mimetype;
        if (pdfFile) {
            mimetype = "application/pdf";
        } else if ("zip".equals(docType)) {
            mimetype = "application/zip";
        } else {
            mimetype = "application/octet-stream";
        }
        args.setRedirectUrl(null);
        return new JSONObject()
                .put("mimetype", mimetype)
                .put("docName", docName)
                .put("fileBytes", results);
    }

    private JSONObject find(JSONArray documents, String documentId) {
        for (int i = 0; i < documents.length(); i++) {
            JSONObject item = documents.getJSONObject(i);
            if (item.getString("documentId").equalsIgnoreCase(documentId)) {
                return item;
            }
        }
        return null;
    }
    // ***DS.snippet.0.end
}

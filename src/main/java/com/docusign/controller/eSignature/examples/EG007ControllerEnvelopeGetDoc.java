package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.common.DocumentType;
import com.docusign.core.model.*;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


/**
 * Download a document from an envelope.<br />
 * An envelope's documents can be downloaded one by one or as a complete set.
 */
@Controller
@RequestMapping("/eg007")
public class EG007ControllerEnvelopeGetDoc extends AbstractEsignatureController {

    private static final String MODEL_DOCUMENTS_OK = "documentsOk";
    private static final String MODEL_DOCUMENT_OPTIONS = "documentOptions";
    private static final String HTTP_CONTENT_DISPOSITION_VALUE = "inline;filename=";
    private static final String ZIP_EXTENSION = "zip";

    private final Session session;
    private final User user;


    @Autowired
    public EG007ControllerEnvelopeGetDoc(DSConfiguration config, Session session, User user) {
        super(config, "eg007", "Download a document");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_ENVELOPE_OK, StringUtils.isNotBlank(session.getEnvelopeId()));
        List<EnvelopeDocumentInfo> envelopeDocuments = session.getEnvelopeDocuments();
        boolean documentsOk = envelopeDocuments != null;
        model.addAttribute(MODEL_DOCUMENTS_OK, documentsOk);
        if (!documentsOk) {
            return;
        }

        ArrayList<OptionItem> documentOptions = new ArrayList<>();
        for (EnvelopeDocumentInfo docInfo : envelopeDocuments) {
            OptionItem doc = new OptionItem(docInfo.getName(), docInfo.getDocumentId());
            documentOptions.add(doc);
        }

        model.addAttribute(MODEL_DOCUMENT_OPTIONS, documentOptions);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
            HttpServletResponse response) throws ApiException, IOException {
        // Step 2 start
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        // Step 2 end
        
        // EnvelopeDocuments::get.
        // Exceptions will be caught by the calling function 
        String documentId = args.getDocSelect();
        // Step 3 start       
        byte[] results = envelopesApi.getDocument(session.getAccountId(), session.getEnvelopeId(), documentId);
        // Step 3 end 

        // Process results
        List<EnvelopeDocumentInfo> envelopeDocuments = session.getEnvelopeDocuments();
        EnvelopeDocumentInfo docItem = find(envelopeDocuments, documentId);

        String docName = docItem.getName();
        String docType = docItem.getType();
        String pdfExtention = DocumentType.PDF.getDefaultFileExtention();
        if (StringUtils.equalsAny(docType, "content", "summary", pdfExtention)) {
            docName = addExtension(docName, pdfExtention);
        }
        if (ZIP_EXTENSION.equals(docType)) {
            docName = addExtension(docName, ZIP_EXTENSION);
        }

        response.setContentType(URLConnection.guessContentTypeFromName(docName));
        response.setContentLength(results.length);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, HTTP_CONTENT_DISPOSITION_VALUE + docName);
        response.getOutputStream().write(results);
        response.flushBuffer();
        return null;
    }

    private static String addExtension(String fileName, String extension) {
        if (FilenameUtils.isExtension(fileName, extension)) {
            return fileName;
        }
        return String.join(".", fileName, extension);
    }

    private static EnvelopeDocumentInfo find(List<EnvelopeDocumentInfo> documents, String documentId) {
        for (EnvelopeDocumentInfo docInfo : documents) {
            if (StringUtils.equalsIgnoreCase(docInfo.getDocumentId(), documentId)) {
                return docInfo;
            }
        }

        throw new ExampleException("Requested document is not found.", null);
    }  
}

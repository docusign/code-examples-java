package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.EnvelopeDocsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.EnvelopeDocumentInfo;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * List an envelope's documents.<br />
 * A <em>Certificate of Completion</em> document is also associated with every
 * envelope. This method is often used to dynamically create a list of an
 * envelope's documents in preparation for enabling your user to download one
 * or more of the documents.
 */
@Controller
@RequestMapping("/eg006")
public class EG006ControllerEnvelopeDocs extends AbstractEsignatureController {

    public EG006ControllerEnvelopeDocs(DSConfiguration config, Session session, User user) {
        super(config, "eg006", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_ENVELOPE_OK, StringUtils.isNotBlank(session.getEnvelopeId()));
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // List the envelope's documents
        //ds-snippet-start:eSign6Step3
        EnvelopeDocumentsResult envelopeDocumentsResult = EnvelopeDocsService.envelopeDocs(
                envelopesApi,
                session.getAccountId(),
                session.getEnvelopeId());
        //ds-snippet-end:eSign6Step3
        
        // Process results
        // Save the envelopeId and its list of documents in the session so
        // they can be used in example 7 (download a document)
        List<EnvelopeDocumentInfo> envelopeDocItems = new ArrayList<>();
        session.setEnvelopeDocuments(envelopeDocItems);
        envelopeDocItems.add(new EnvelopeDocumentInfo("Combined", "content", "combined"));
        envelopeDocItems.add(new EnvelopeDocumentInfo("Zip archive", "zip", "archive"));

        for (EnvelopeDocument doc : envelopeDocumentsResult.getEnvelopeDocuments()) {
            String documentName = doc.getName();
            if (StringUtils.equals(doc.getDocumentId(), "certificate")) {
                documentName = "Certificate of completion";
            }
            envelopeDocItems.add(new EnvelopeDocumentInfo(documentName, doc.getType(), doc.getDocumentId()));
        }

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(envelopeDocumentsResult)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}

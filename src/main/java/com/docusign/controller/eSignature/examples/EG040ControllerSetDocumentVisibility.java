package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.SetDocumentVisibilityService;
import com.docusign.core.common.Utils;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/eg040")
public class EG040ControllerSetDocumentVisibility extends AbstractEsignatureController {
    private static final String DOCUMENT_FILE_NAME_PDF = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_FILE_NAME_DOCX = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";

    private static final String DOCUMENT_FILE_NAME_HTML = "doc_1.html";

    private static final String CASE_FOR_INSTRUCTIONS = "ACCOUNT_LACKS_PERMISSIONS";

    private static final String FIXING_INSTRUCTIONS_FOR_PERMISSIONS = "See " +
            "<a href=\"https://developers.docusign.com/docs/esign-rest-api/how-to/set-document-visibility\">" +
            "How to set document visibility for envelope recipients</a> in the Docusign Developer Center " +
            "for instructions on how to enable document visibility in your developer account.";

    public EG040ControllerSetDocumentVisibility(DSConfiguration config, Session session, User user) {
        super(config, "eg040", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        if (Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId())) {
            session.setStatusCFR("enabled");
            throw new Exception(config.getCodeExamplesText().getSupportingTexts().getCFRError());
        }
        super.onInitModel(args, model);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        model.addAttribute("fixingInstructions", FIXING_INSTRUCTIONS_FOR_PERMISSIONS);
        model.addAttribute("caseForInstructions", CASE_FOR_INSTRUCTIONS);

        String basePath = session.getBasePath();
        String accessToken = user.getAccessToken();

        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String signer2Name = args.getSignerName2();
        String signer2Email = args.getSignerEmail2();
        String ccName = args.getCcName();
        String ccEmail = args.getCcEmail();
        String accountId = session.getAccountId();

        EnvelopeDefinition envelope = SetDocumentVisibilityService.makeEnvelope(signerEmail, signerName, signer2Email,
                signer2Name, ccEmail, ccName, DOCUMENT_FILE_NAME_PDF, DOCUMENT_FILE_NAME_DOCX, DOCUMENT_FILE_NAME_HTML);

        //ds-snippet-start:eSign40Step2
        ApiClient apiClient = createApiClient(basePath, accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        //ds-snippet-end:eSign40Step2

        //ds-snippet-start:eSign40Step4
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);
        //ds-snippet-end:eSign40Step4

        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + envelopeSummary.getEnvelopeId() + ".")
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}

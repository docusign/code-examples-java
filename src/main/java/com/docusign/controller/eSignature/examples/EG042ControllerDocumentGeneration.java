package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.DocumentGenerationService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import static com.docusign.controller.eSignature.examples.EG042ControllerDocumentGeneration.EG_042;

@Controller
@RequestMapping(EG_042)
public class EG042ControllerDocumentGeneration extends AbstractEsignatureController {
    public static final String EG_042 = "/eg042";

    private static final String DOCUMENT_FILE_NAME = "Offer_Letter_Dynamic_Table.docx";

    public EG042ControllerDocumentGeneration(DSConfiguration config, Session session, User user) {
        super(config, EG_042, session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws Exception {
        String basePath = session.getBasePath();
        String accessToken = user.getAccessToken();

        EnvelopesApi envelopesApi = createEnvelopesApi(basePath, accessToken);
        TemplatesApi templatesApi = createTemplatesApi(basePath, accessToken);

        String envelopeId = (new DocumentGenerationService()).generateDocument(
                session.getAccountId(),
                args.getCandidateEmail(),
                args.getCandidateName(),
                args.getManagerName(),
                args.getJobTitle(),
                args.getSalary(),
                args.getRsus(),
                args.getStartDate(),
                DOCUMENT_FILE_NAME,
                envelopesApi,
                templatesApi);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeId)
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}

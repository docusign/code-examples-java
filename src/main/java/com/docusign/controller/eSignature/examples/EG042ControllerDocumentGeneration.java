package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.DocumentGenerationService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/eg042")
public class EG042ControllerDocumentGeneration extends AbstractEsignatureController {

    private static final String DOCUMENT_FILE_NAME = "Offer_Letter_Demo.docx";
    private final Session session;
    private final User user;

    @Autowired
    public EG042ControllerDocumentGeneration(DSConfiguration config, Session session, User user) {
        super(config, "eg042");
        this.session = session;
        this.user = user;
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

package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.ScheduledSendlingService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeDelayRule;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ScheduledSending;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.Workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Send an envelope with a remote (email) signer and cc recipient.<br />
 * The envelope includes a pdf, Word, and HTML document. Anchor text is used to
 * position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg035")
public class EG035ControllerScheduledSending extends AbstractEsignatureController {
    private final Session session;
    private final User user;

    @Autowired
    public EG035ControllerScheduledSending(DSConfiguration config, Session session, User user) {
        super(config, "eg035");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Set status for the makeEnvelope method
        if (!EnvelopeHelpers.ENVELOPE_STATUS_CREATED.equalsIgnoreCase(args.getStatus())) {
            args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        }

        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());


        System.out.println("RESUMEDATE");
        System.out.println(args.getResumeDate()+"T00:00:00Z");

        EnvelopeDefinition envelope = ScheduledSendlingService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                args.getResumeDate(),
                args.getStatus());

        // Step 3 start
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);
        // Step 3 end

        System.out.println("ENVELOPE");
        System.out.println(results.getEnvelopeId());

        // process results
        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(this.codeExampleText.ExampleName)
                .withMessage(this.codeExampleText.ResultsPageText
                        .replaceFirst("\\{0}", results.getEnvelopeId())
                )
                .withJsonObject(results)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }


}

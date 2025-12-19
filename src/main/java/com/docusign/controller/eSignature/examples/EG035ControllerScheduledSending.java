package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.ScheduledSendlingService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Send an envelope with a remote (email) signer and cc recipient.<br />
 * The envelope includes a pdf, Word, and HTML document. Anchor text is used to
 * position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg035")
public class EG035ControllerScheduledSending extends AbstractEsignatureController {

    public EG035ControllerScheduledSending(DSConfiguration config, Session session, User user) {
        super(config, "eg035", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
            HttpServletResponse response) throws ApiException, IOException {
        // Set status for the makeEnvelope method
        if (!EnvelopeHelpers.ENVELOPE_STATUS_CREATED.equalsIgnoreCase(args.getStatus())) {
            args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        }

        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        EnvelopeDefinition envelope = ScheduledSendlingService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                args.getResumeDate(),
                args.getStatus());

        // Step 3 start
        //ds-snippet-start:eSign35Step3
        var results = envelopesApi.createEnvelopeWithHttpInfo(
                session.getAccountId(),
                envelope,
                envelopesApi.new CreateEnvelopeOptions());
        Map<String, List<String>> headers = results.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        //ds-snippet-end:eSign35Step3
        // Step 3 end

        // process results
        session.setEnvelopeId(results.getData().getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", results.getData().getEnvelopeId()))
                .withJsonObject(results)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}

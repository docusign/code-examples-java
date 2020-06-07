package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.TemplateRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;

@Controller
@RequestMapping("/eg009")
public class EG009ControllerUseTemplate extends EGController {

    private String message;

    @Override
    protected void addSpecialAttributes(ModelMap model) {
        model.addAttribute("templateOk", session.getAttribute("templateId") != null);
    }

    @Override
    protected String getResponseTitle() {
        return "Envelope sent";
    }

    @Override
    protected String getEgName() {
        return "eg009";
    }

    @Override
    protected String getTitle() {
        return "Send envelope using a template";
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            String accessToken, String basePath) throws ApiException {
        // Data for this method
        // accessToken    (argument)
        // basePath       (argument)
        String accountId = args.getAccountId();


        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeDefinition envelope = makeEnvelope(args);
        EnvelopeSummary result = envelopesApi.createEnvelope(accountId, envelope);
        // process result
        session.setAttribute("envelopeId", result.getEnvelopeId());
        setMessage("The envelope has been created and sent!<br/>Envelope ID " + result.getEnvelopeId() + ".");
        return result;
    }

    private EnvelopeDefinition makeEnvelope(WorkArguments args) {
        // Data for this method
        String signerEmail = args.getSignerEmail();
        String signerName = args.getSignerName();
        String ccEmail = args.getCcEmail();
        String ccName = args.getCcName();
        String templateId = args.getTemplateId();


        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setTemplateId(templateId);

        TemplateRole signer1 = new TemplateRole();
        signer1.setEmail(signerEmail);
        signer1.setName(signerName);
        signer1.setRoleName("signer");

        TemplateRole cc1 = new TemplateRole();
        cc1.setEmail(ccEmail);
        cc1.setName(ccName);
        cc1.setRoleName("cc");

        env.setTemplateRoles(Arrays.asList(signer1, cc1));
        env.setStatus("sent");
        return env;
    }
    // ***DS.snippet.0.end
}

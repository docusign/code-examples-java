package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.CollectPaymentService;
import com.docusign.core.common.Utils;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Send an envelope with an order form, including a payment field.<br/>
 * This class programmatically constructs the order form. For many use cases,
 * it would be better to create the order form as a template using the DocuSign
 * web tool as a WYSIWYG form designer. <br/>
 * <b>Note:</b> This example will only work if the sender's DocuSign account is
 * set up with a DocuSign payment gateway. Next properties should are set:
 * <ul>
 * <li>Gateway_Account_Id</li>
 * <li>Gateway_Name</li>
 * <li>Gateway_Display_Name</li>
 * </ul>
 * Since the Payment Gateway ID is set in the configuration file, you will need
 * to run your own instance of this project to set it.
 */
@Controller
@RequestMapping("/eg014")
public class EG014ControllerCollectPayment extends AbstractEsignatureController {

    private static final String MODEL_GATEWAY_OK = "gatewayOk";

    public EG014ControllerCollectPayment(DSConfiguration config, Session session, User user) {
        super(config, "eg014", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        if (Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId())) {
            session.setStatusCFR("enabled");
            throw new Exception(config.getCodeExamplesText().getSupportingTexts().getCFRError());
        }
        model.addAttribute(MODEL_GATEWAY_OK, null != config.getGatewayAccountId());
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 1. Make the envelope request body
        EnvelopeDefinition envelope = CollectPaymentService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                args.getCcName(),
                args.getCcEmail(),
                config.getGatewayAccountId(),
                config.getGatewayName(),
                config.getGatewayDisplayName(),
                args);

        // Step 2. call Envelopes::create API method
        EnvelopeSummary envelopeSummary = CollectPaymentService.collectPayment(envelopesApi, envelope, session.getAccountId());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeSummary.getEnvelopeId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}

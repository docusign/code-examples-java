package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.SendBinaryDocsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Send an envelope using binary document transfer.<br />
 * The envelope includes a pdf, doc, and HTML document. Multipart data transfer
 * is used to send the documents in binary format to DocuSign.
 */
@Controller
@RequestMapping("/eg010")
public class EG010ControllerSendBinaryDocs extends AbstractEsignatureController {

    public EG010ControllerSendBinaryDocs(DSConfiguration config, Session session, User user) {
        super(config, "eg010", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws IOException {
        String responseString = SendBinaryDocsService.sendBinaryDocs(
                args,
                args.getSignerName(),
                args.getSignerEmail(),
                args.getCcName(),
                args.getCcEmail(),
                session.getBasePath(),
                session.getAccountId(),
                user.getAccessToken()
        );
        JSONObject obj = new JSONObject(responseString);
        String envelopeId = obj.getString("envelopeId");
        session.setEnvelopeId(envelopeId);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeId)
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}

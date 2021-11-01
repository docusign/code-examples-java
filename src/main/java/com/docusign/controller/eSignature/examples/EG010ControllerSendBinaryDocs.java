package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.services.eSignature.examples.SendBinaryDocsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Session session;
    private final User user;

    @Autowired
    public EG010ControllerSendBinaryDocs(DSConfiguration config, Session session, User user) {
        super(config, "eg010", "Send envelope with multipart mime");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
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

        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br/>Envelope ID " + envelopeId + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}

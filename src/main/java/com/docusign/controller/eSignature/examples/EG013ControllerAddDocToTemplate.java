package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.common.DocumentType;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.CompositeTemplate;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.InlineTemplate;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ServerTemplate;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;


/**
 * Use embedded signing from a template with an added document. <br/>
 * This example sends an envelope based on a template. In addition to the
 * template's document(s), the example adds an additional document to the
 * envelope by using the Composite Templates.
 */
@Controller
@RequestMapping("/eg013")
public class EG013ControllerAddDocToTemplate extends AbstractEsignatureController {

    private static final String MODEL_LIST_TEMPLATE = "listTemplates";
    private static final String SIGNER_CLIENT_ID = "1000";
    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon2.ftl";
    private static final String HTML_DOCUMENT_NAME = "Appendix 1--Sales order";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;

    private final Session session;
    private final User user;


    @Autowired
    public EG013ControllerAddDocToTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg013", "Use embedded signing from template and extra doc");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        EnvelopeTemplateResults templates = templatesApi.listTemplates(session.getAccountId());
        model.addAttribute(MODEL_LIST_TEMPLATE, templates.getEnvelopeTemplates());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
            HttpServletResponse response) throws ApiException, IOException {
        String accountId = session.getAccountId();
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 2 start
        args.setDsReturnUrl(config.getDsReturnUrl());
        args.setDsPingUrl(config.getDsPingUrl());
        args.setSignerClientId(SIGNER_CLIENT_ID);
        EnvelopeDefinition envelope = makeEnvelope(args);
        // Step 2 end
        
        // Step 3 start
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
        // Step 3 end
        
        // Step 4 start
        RecipientViewRequest viewRequest = makeRecipientViewRequest(args);
        ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, results.getEnvelopeId(), viewRequest);
        return new RedirectView(viewUrl.getUrl());
        // Step 4 end
    }

    private static RecipientViewRequest makeRecipientViewRequest(WorkArguments args) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        viewRequest.setReturnUrl(args.getDsReturnUrl());
        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        viewRequest.setAuthenticationMethod("none");
        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(args.getSignerEmail());
        viewRequest.setUserName(args.getSignerName());
        viewRequest.setClientUserId(args.getSignerClientId());

        // DocuSign recommends that you redirect to DocuSign for the
        // embedded signing. There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        viewRequest.setPingFrequency("600");
        viewRequest.setPingUrl(args.getDsPingUrl());

        return viewRequest;
    }

    // The envelope request object uses Composite Template to include in the envelope:
    // 1. A template stored on the DocuSign service
    // 2. An additional document which is a custom HTML source document
    private EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {
        // Create a signer and cc recipients for the signer role of the server template
        CarbonCopy cc1 = new CarbonCopy();
        cc1.setEmail(args.getCcEmail());
        cc1.setName(args.getCcName());
        cc1.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc1.setRecipientId("2");

        // create a composite template for the Server Template
        CompositeTemplate compTemplate1 = new CompositeTemplate();
        compTemplate1.setCompositeTemplateId("1");
        ServerTemplate serverTemplates = new ServerTemplate();
        serverTemplates.setSequence("1");
        serverTemplates.setTemplateId(args.getTemplateId());
        compTemplate1.setServerTemplates(Arrays.asList(serverTemplates));

        // Add the roles via an inlineTemplate
        InlineTemplate inlineTemplate = new InlineTemplate();
        inlineTemplate.setSequence("2");
        inlineTemplate.setRecipients(EnvelopeHelpers.createRecipients(createSigner(args), cc1));
        compTemplate1.setInlineTemplates(Arrays.asList(inlineTemplate));

        // The signer recipient for the added document with a tab definition:
        Tabs signer1Tabs = EnvelopeHelpers.createSingleSignerTab("**signature_1**", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        Signer signer1AddedDoc = createSigner(args);
        signer1AddedDoc.setTabs(signer1Tabs);

        // create the HTML document
        byte[] htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args);

        // create a composite template for the added document and add the recipients via an inlineTemplate
        CompositeTemplate compTemplate2 = new CompositeTemplate();
        compTemplate2.setCompositeTemplateId("2");
        InlineTemplate inlineTemplate2 = new InlineTemplate();
        inlineTemplate2.setSequence("1");
        inlineTemplate2.setRecipients(EnvelopeHelpers.createRecipients(signer1AddedDoc, cc1));
        compTemplate2.setInlineTemplates(Arrays.asList(inlineTemplate2));
        compTemplate2.setDocument(EnvelopeHelpers.createDocument(htmlDoc, HTML_DOCUMENT_NAME,
                DocumentType.HTML.getDefaultFileExtention(), "1"));

        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        env.setCompositeTemplates(Arrays.asList(compTemplate1, compTemplate2));

        return env;
    }

    // Adding clientUserId transforms the template recipient into an embedded recipient
    private static Signer createSigner(WorkArguments args) {
        Signer signer = new Signer();
        signer.setEmail(args.getSignerEmail());
        signer.setName(args.getSignerName());
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId("1");
        signer.setClientUserId(args.getSignerClientId());
        return signer;
    }
}

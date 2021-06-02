package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;


/**
 * Applying a brand to an envelope<br />
 * The envelope includes a pdf document. Anchor text <i>AutoPlace</i> is used
 * to position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg029")
public class EG029ControllerApplyBrandToEnvelope extends AbstractEsignatureController {

    private static final String MODEL_LIST_BRAND = "listBrands";
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "EG025 Lorem Ipsum";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;

    private final Session session;
    private final User user;


    @Autowired
    public EG029ControllerApplyBrandToEnvelope(DSConfiguration config, Session session, User user) {
        super(config, "eg029", "Apply brand to envelope");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());
        BrandsResponse brands = accountsApi.listBrands(session.getAccountId());
        model.addAttribute(MODEL_LIST_BRAND, brands.getBrands());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = makeEnvelope(args);

        // Step 5: Call the eSignature REST API
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        DoneExample.createDefault(title)
                .withJsonObject(envelopeSummary)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                    + envelopeSummary.getEnvelopeId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    // Creates an envelope. The envelope has one recipient who should sign an
    // attached document. Attached document is read from a local directory.
    // Also the envelope contains a brand Id which is created on EG028 example.
    private static EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {
        // Reads a file from a local directory and create Document object.
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        // Create a signer recipient to sign the document and associate sign tab
        Tabs tabs = EnvelopeHelpers.createSingleSignerTab("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        Signer signer = new Signer()
                .email(args.getSignerEmail())
                .name(args.getSignerName())
                .recipientId("1")
                .routingOrder("1")
                .tabs(tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));

        // Return envelope definition using created objects: the document,
        // the recipients and obtained brand Id.
        return new EnvelopeDefinition()
                .emailSubject("EG029. Please Sign")
                .documents(List.of(document))
                .recipients(recipients)
                .brandId(args.getBrandId())
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
}

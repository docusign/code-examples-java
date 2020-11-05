package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.Checkbox;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeTemplate;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.List;
import com.docusign.esign.model.ListItem;
import com.docusign.esign.model.Radio;
import com.docusign.esign.model.RadioGroup;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.TemplateSummary;
import com.docusign.esign.model.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;


/**
 * Create a template.<br />
 * Create a template with two roles, <b>signer</b> and <b>cc</b>. The template
 * includes three documents.
 */
@Controller
@RequestMapping("/eg008")
public class EG008ControllerCreateTemplate extends AbstractEsignatureController {

    private static final String TEMPLATE_NAME = "Example Signer and CC template";
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_fields.pdf";
    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";
    private static final String DOCUMENT_ID = "1";
    private static final String PAGE_NUMBER = "1";
    private static final String FALSE = "false";

    private final Session session;
    private final User user;


    @Autowired
    public EG008ControllerCreateTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg008", "Create a template");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Step 1. list existing templates
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        TemplatesApi.ListTemplatesOptions options = templatesApi.new ListTemplatesOptions();
        options.setSearchText(TEMPLATE_NAME);
        String accountId = session.getAccountId();
        EnvelopeTemplateResults results = templatesApi.listTemplates(accountId, options);

        // Step 2. Process results. If template do not exist, create one
        if (Integer.parseInt(results.getResultSetSize()) > 0) {
            EnvelopeTemplate template = results.getEnvelopeTemplates().get(0);
            DoneExample.createDefault(title)
                    .withMessage(String.format(
                            "The template already exists in your account. <br/>Template name: %s, ID %s.",
                             template.getName(), template.getTemplateId()))
                    .addToModel(model);
        } else {
            session.setTemplateName(TEMPLATE_NAME);
            TemplateSummary template = templatesApi.createTemplate(accountId, makeTemplate());
            DoneExample.createDefault(title)
                    .withMessage(String.format(
                            "The template has been created!<br/>Template name: %s, ID %s.",
                            template.getName(), template.getTemplateId()))
                    .addToModel(model);
        }

        return DONE_EXAMPLE_PAGE;
    }

    // document 1 (pdf) has tag /sn1/
    //
    // The template has two recipient roles.
    // recipient 1 - signer
    // recipient 2 - cc
    // The template will be sent first to the signer.
    // After it is signed, a copy is sent to the cc person.
    private EnvelopeTemplate makeTemplate() throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "1");

        // Tabs are set per recipient / signer
        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setCheckboxTabs(Arrays.asList(
                createCheckbox("ckAuthorization", "75", "417"),
                createCheckbox("ckAuthentication", "75", "447"),
                createCheckbox("ckAgreement", "75", "478"),
                createCheckbox("ckAcknowledgement", "75", "508")));
        signer1Tabs.setListTabs(Arrays.asList(createList()));
        signer1Tabs.setRadioGroupTabs(Arrays.asList(createRadioGroup()));
        signer1Tabs.setSignHereTabs(Arrays.asList(createSignHere()));
        signer1Tabs.textTabs(Arrays.asList(
                createText("text", "153", "230"),
                createText("numbersOnly", "153", "260")));

        // create a signer recipient to sign the document, identified by name and email
        // routingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.
        Signer signer = new Signer();
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signer1Tabs);

        // create a cc recipient to receive a copy of the documents, identified by name and email
        CarbonCopy cc1 = new CarbonCopy();
        cc1.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc1.setRoutingOrder("2");
        cc1.setRecipientId("2");

        // Create the overall template definition. The order in the docs array
        // determines the order in the envelope.
        EnvelopeTemplate template = new EnvelopeTemplate();
        template.setDocuments(Arrays.asList(doc));
        template.setEmailSubject("Please sign this document");
        template.setName(session.getTemplateName());
        template.setDescription("Example template created via the API");
        template.setShared(FALSE);
        template.setRecipients(EnvelopeHelpers.createRecipients(signer, cc1));
        template.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);

        return template;
    }

    private static List createList() {
        List list = new List();
        list.setDocumentId(DOCUMENT_ID);
        list.setPageNumber(PAGE_NUMBER);
        list.setXPosition("142");
        list.setYPosition("291");
        list.setFont("helvetica");
        list.setFontSize("size14");
        list.setTabLabel("list");
        list.setRequired(FALSE);
        list.setListItems(Arrays.asList(
                createListItem("Red"),
                createListItem("Orange"),
                createListItem("Yellow"),
                createListItem("Green"),
                createListItem("Blue"),
                createListItem("Indigo"),
                createListItem("Violet")
        ));
        return list;
    }

    private static ListItem createListItem(String color) {
        ListItem item = new ListItem();
        item.setText(color);
        item.setValue(color.toLowerCase(Locale.ENGLISH));
        return item;
    }

    private static RadioGroup createRadioGroup() {
        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setDocumentId(DOCUMENT_ID);
        radioGroup.setGroupName("radio1");

        radioGroup.setRadios(Arrays.asList(
                createRadio("white", "142"),
                createRadio("red", "74"),
                createRadio("blue", "220")
        ));
        return radioGroup;
    }

    private static Radio createRadio(String value, String xPosition) {
        Radio radio = new Radio();
        radio.setPageNumber(PAGE_NUMBER);
        radio.setValue(value);
        radio.setXPosition(xPosition);
        radio.setYPosition("384");
        radio.setRequired(FALSE);
        return radio;
    }

    private static Checkbox createCheckbox(String label, String xPosition, String yPosition) {
        Checkbox check = new Checkbox();
        check.setDocumentId(DOCUMENT_ID);
        check.setPageNumber(PAGE_NUMBER);
        check.setXPosition(xPosition);
        check.setYPosition(yPosition);
        check.setTabLabel(label);
        return check;
    }

    private static Text createText(String label, String xPosition, String yPosition) {
        Text text = new Text();
        text.setDocumentId(DOCUMENT_ID);
        text.setPageNumber(PAGE_NUMBER);
        text.setXPosition(xPosition);
        text.setYPosition(yPosition);
        text.setFont("helvetica");
        text.setFontSize("size14");
        text.setTabLabel(label);
        text.setHeight("23");
        text.setWidth("84");
        text.setRequired(FALSE);
        return text;
    }

    private static SignHere createSignHere() {
        SignHere signHere = new SignHere();
        signHere.setDocumentId(DOCUMENT_ID);
        signHere.setPageNumber(PAGE_NUMBER);
        signHere.setXPosition("191");
        signHere.setYPosition("148");
        return signHere;
    }
    // ***DS.snippet.0.end
}

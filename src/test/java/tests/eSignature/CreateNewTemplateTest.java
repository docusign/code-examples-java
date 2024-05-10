package tests.eSignature;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.CreateTemplateService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public final class CreateNewTemplateTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String TEMPLATE_NAME = "Example Signer and CC template v2";

    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_fields.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final String DOCUMENT_ID = "1";

    private static final String PAGE_NUMBER = "1";

    private static final String FALSE = "false";

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final EnvelopesApi envelopesApi;

    private final TestConfig testConfig;

    public CreateNewTemplateTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.envelopesApi = new EnvelopesApi(apiClient);
    }

    @Test
    public void SearchTemplatesByName_CorrectInputValues_EnvelopeTemplateResults() throws ApiException {
        // Act
        EnvelopeTemplateResults envelopeTemplateResults = CreateTemplateService.searchTemplatesByName(
                apiClient,
                testConfig.getAccountId(),
                TEMPLATE_NAME);

        // Assert
        Assertions.assertNotNull(envelopeTemplateResults);
        Assertions.assertTrue(Integer.parseInt(envelopeTemplateResults.getResultSetSize()) > 0);

        EnvelopeTemplate template = envelopeTemplateResults.getEnvelopeTemplates().get(0);
        testConfig.setTemplateId(template.getTemplateId());
    }

    @Test
    public void CreateTemplate_CorrectInputValues_TemplateSummary() throws IOException, ApiException {
        // Arrange
        EnvelopeTemplate envelopeTemplate = CreateTemplateService.makeTemplate(TEMPLATE_NAME);

        // Act
        TemplateSummary template = CreateTemplateService.createTemplate(
                apiClient,
                testConfig.getAccountId(),
                envelopeTemplate
        );

        // Assert
        Assertions.assertNotNull(template);
        Assertions.assertNotNull(template.getTemplateId());

        testConfig.setTemplateId(template.getTemplateId());
    }

    @Test
    public void MakeRecipientViewRequest_CorrectInputValues_RecipientViewRequest() throws ApiException {
        // Act
        EnvelopeTemplateResults envelopeTemplateResults = CreateTemplateService.searchTemplatesByName(
                apiClient,
                testConfig.getAccountId(),
                TEMPLATE_NAME);

        // Assert
        Assertions.assertNotNull(envelopeTemplateResults);
        Assertions.assertTrue(Integer.parseInt(envelopeTemplateResults.getResultSetSize()) > 0);

        EnvelopeTemplate template = envelopeTemplateResults.getEnvelopeTemplates().get(0);
        testConfig.setTemplateId(template.getTemplateId());
    }

    @Test
    public void MakeTemplate_CorrectInputValues_EnvelopeTemplate() throws IOException {
        // Assert
        String defaultIdOne = "1";
        Document doc = EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, defaultIdOne);
        String emailSubject = "Please sign this document";
        String description = "Example template created via the API";
        String falseString = "false";
        String textXPosition = "153";
        String checkboxXPosition = "75";
        String defaultIdTwo = "2";

        Tabs signerTabs = new Tabs();
        signerTabs.setCheckboxTabs(Arrays.asList(
                CreateTemplateService.createCheckbox("ckAuthorization", checkboxXPosition, "417"),
                CreateTemplateService.createCheckbox("ckAuthentication", checkboxXPosition, "447"),
                CreateTemplateService.createCheckbox("ckAgreement", checkboxXPosition, "478"),
                CreateTemplateService.createCheckbox("ckAcknowledgement", checkboxXPosition, "508")));

        signerTabs.setListTabs(Collections.singletonList(CreateTemplateService.createList()));
        signerTabs.setRadioGroupTabs(Collections.singletonList(CreateTemplateService.createRadioGroup()));
        signerTabs.setSignHereTabs(Collections.singletonList(CreateTemplateService.createSignHere()));
        signerTabs.textTabs(java.util.List.of(
                CreateTemplateService.createText("text", textXPosition, "230")
        ));
        signerTabs.numericalTabs(java.util.List.of(
                CreateTemplateService.createNumerical("numericalCurrency", textXPosition, "260")
        ));

        Signer signer = new Signer();
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId(defaultIdOne);
        signer.setRoutingOrder(defaultIdOne);
        signer.setTabs(signerTabs);

        CarbonCopy cc = new CarbonCopy();
        cc.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc.setRoutingOrder(defaultIdTwo);
        cc.setRecipientId(defaultIdTwo);

        EnvelopeTemplate expectedEnvelopeTemplate = new EnvelopeTemplate();
        expectedEnvelopeTemplate.setDocuments(Collections.singletonList(doc));
        expectedEnvelopeTemplate.setEmailSubject(emailSubject);
        expectedEnvelopeTemplate.setName(TEMPLATE_NAME);
        expectedEnvelopeTemplate.setDescription(description);
        expectedEnvelopeTemplate.setShared(falseString);
        expectedEnvelopeTemplate.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        expectedEnvelopeTemplate.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);

        // Act
        EnvelopeTemplate envelopeTemplate = CreateTemplateService.makeTemplate(TEMPLATE_NAME);

        // Assert
        Assertions.assertNotNull(envelopeTemplate);
        Assertions.assertEquals(expectedEnvelopeTemplate, envelopeTemplate);
    }

    @Test
    public void CreateList_CorrectInputValues_List() {
        // Arrange
        List expectedList = new List();
        expectedList.setDocumentId(DOCUMENT_ID);
        expectedList.setPageNumber(PAGE_NUMBER);
        expectedList.setXPosition("142");
        expectedList.setYPosition("291");
        expectedList.setFont("helvetica");
        expectedList.setFontSize("size14");
        expectedList.setTabLabel("list");
        expectedList.setRequired(FALSE);
        expectedList.setListItems(Arrays.asList(
                CreateTemplateService.createListItem("Red"),
                CreateTemplateService.createListItem("Orange"),
                CreateTemplateService.createListItem("Yellow"),
                CreateTemplateService.createListItem("Green"),
                CreateTemplateService.createListItem("Blue"),
                CreateTemplateService.createListItem("Indigo"),
                CreateTemplateService.createListItem("Violet")
        ));

        // Act
        List list = CreateTemplateService.createList();

        // Assert
        Assertions.assertNotNull(list);
        Assertions.assertEquals(expectedList, list);
    }

    @Test
    public void CreateListItem_CorrectInputValues_ListItem() {
        // Arrange
        String color = "blue";

        ListItem expectedListItem = new ListItem();
        expectedListItem.setText(color);
        expectedListItem.setValue(color.toLowerCase(Locale.ENGLISH));

        // Act
        ListItem listItem = CreateTemplateService.createListItem(color);

        // Assert
        Assertions.assertNotNull(listItem);
        Assertions.assertEquals(expectedListItem, listItem);
    }

    @Test
    public void CreateRadioGroup_CorrectInputValues_RadioGroup() {
        // Arrange
        String white = "white";
        String red = "red";
        String blue = "blue";
        String blueXPosition = "220";
        String redXPosition = "74";
        String whiteXPosition = "142";
        String groupName = "radio1";

        RadioGroup expectedRadioGroup = new RadioGroup();
        expectedRadioGroup.setDocumentId(DOCUMENT_ID);
        expectedRadioGroup.setGroupName(groupName);

        expectedRadioGroup.setRadios(Arrays.asList(
                CreateTemplateService.createRadio(white, whiteXPosition),
                CreateTemplateService.createRadio(red, redXPosition),
                CreateTemplateService.createRadio(blue, blueXPosition)
        ));

        // Act
        RadioGroup radioGroup = CreateTemplateService.createRadioGroup();

        // Assert
        Assertions.assertNotNull(radioGroup);
        Assertions.assertEquals(expectedRadioGroup, radioGroup);
    }

    @Test
    public void CreateRadio_CorrectInputValues_Radio() {
        // Arrange
        String value = "blue";
        String xPosition = "111";
        String yPosition = "384";

        Radio expectedRadio = new Radio();
        expectedRadio.setPageNumber(PAGE_NUMBER);
        expectedRadio.setValue(value);
        expectedRadio.setXPosition(xPosition);
        expectedRadio.setYPosition(yPosition);
        expectedRadio.setRequired(FALSE);

        // Act
        Radio radio = CreateTemplateService.createRadio(value, xPosition);

        // Assert
        Assertions.assertNotNull(radio);
        Assertions.assertEquals(expectedRadio, radio);
    }

    @Test
    public void CreateCheckbox_CorrectInputValues_Checkbox() {
        // Arrange
        String label = "blue";
        String yPosition = "111";
        String xPosition = "111";

        Checkbox expectedCheckBox = new Checkbox();
        expectedCheckBox.setDocumentId(DOCUMENT_ID);
        expectedCheckBox.setPageNumber(PAGE_NUMBER);
        expectedCheckBox.setXPosition(xPosition);
        expectedCheckBox.setYPosition(yPosition);
        expectedCheckBox.setTabLabel(label);

        // Act
        Checkbox check = CreateTemplateService.createCheckbox(label, xPosition, yPosition);

        // Assert
        Assertions.assertNotNull(check);
        Assertions.assertEquals(expectedCheckBox, check);
    }

    @Test
    public void CreateText_CorrectInputValues_Text() {
        // Arrange
        String xPosition = "1";
        String yPosition = "5";
        String label = "blue";
        String font = "helvetica";
        String fontSize = "size14";
        String height = "23";
        String width = "84";

        Text expectedText = new Text();
        expectedText.setDocumentId(DOCUMENT_ID);
        expectedText.setPageNumber(PAGE_NUMBER);
        expectedText.setXPosition(xPosition);
        expectedText.setYPosition(yPosition);
        expectedText.setFont(font);
        expectedText.setFontSize(fontSize);
        expectedText.setTabLabel(label);
        expectedText.setHeight(height);
        expectedText.setWidth(width);
        expectedText.setRequired(FALSE);

        // Act
        Text text = CreateTemplateService.createText(label, xPosition, yPosition);

        // Assert
        Assertions.assertNotNull(text);
        Assertions.assertEquals(expectedText, text);
    }

    @Test
    public void CreateSignHere_CorrectInputValues_SignHere() {
        // Arrange
        String xPosition = "191";
        String yPosition = "148";

        SignHere expectedSignHere = new SignHere();
        expectedSignHere.setDocumentId(DOCUMENT_ID);
        expectedSignHere.setPageNumber(PAGE_NUMBER);
        expectedSignHere.setXPosition(xPosition);
        expectedSignHere.setYPosition(yPosition);

        // Act
        SignHere signHere = CreateTemplateService.createSignHere();

        // Assert
        Assertions.assertNotNull(signHere);
        Assertions.assertEquals(expectedSignHere, signHere);
    }
}

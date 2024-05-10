package tests.eSignature;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.SetTabValuesService;
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
import java.util.List;

public final class SetTabValuesTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_salary.docx";

    private static final String DOCUMENT_NAME = "Lorem Ipsum";

    private static final String SIGNER_CLIENT_ID = "1000";

    private static final String basePathAddition = "/restapi";

    private final String RedirectUrl = "https://developers.docusign.com/platform/auth/consent";

    private final ApiClient apiClient;

    private final EnvelopesApi envelopesApi;

    private final TestConfig testConfig;

    public SetTabValuesTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.envelopesApi = new EnvelopesApi(apiClient);
    }

    @Test
    public void MakeEnvelopeWithTabValues_CorrectInputValues_EnvelopeDefinition() throws IOException {
        // Arrange
        String emailSubject = "Please sign this document from the Java SDK";
        String initialId = "1";
        Integer salary = 123000;
        String salaryString = "salary";
        String fontSize = "size11";
        String font = "helvetica";
        String trueString = "true";
        String falseString = "false";

        Signer signer = new Signer();
        signer.setEmail(testConfig.getSignerEmail());
        signer.setName(testConfig.getSignerName());
        signer.clientUserId(SIGNER_CLIENT_ID);
        signer.recipientId(initialId);

        SignHere signHere = new SignHere();
        signHere.setAnchorString("/sn1");
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorYOffset("10");
        signHere.setAnchorXOffset("20");

        Text textLegal = new Text();
        textLegal.setAnchorString("/legal/");
        textLegal.setAnchorUnits("pixels");
        textLegal.setAnchorYOffset("-9");
        textLegal.setAnchorXOffset("5");
        textLegal.setFont(font);
        textLegal.setFontSize(fontSize);
        textLegal.setBold(trueString);
        textLegal.setValue(testConfig.getSignerName());
        textLegal.setLocked(falseString);
        textLegal.setTabId("legal_name");
        textLegal.setTabLabel("Legal Name");

        Text textFamiliar = new Text();
        textFamiliar.setAnchorString("/familiar/");
        textFamiliar.setAnchorUnits("pixels");
        textFamiliar.setAnchorYOffset("-9");
        textFamiliar.setAnchorXOffset("5");
        textFamiliar.setFont(font);
        textFamiliar.setFontSize(fontSize);
        textFamiliar.setBold(trueString);
        textFamiliar.setValue(testConfig.getSignerName());
        textFamiliar.setLocked(falseString);
        textFamiliar.setTabId("familiar_name");
        textFamiliar.setTabLabel("Familiar Name");

        Numerical numericalSalary = new Numerical();
        numericalSalary.setValidationType("Currency");
        numericalSalary.setPageNumber(initialId);
        numericalSalary.setDocumentId(initialId);
        numericalSalary.setXPosition("210");
        numericalSalary.setYPosition("235");
        numericalSalary.setHeight("20");
        numericalSalary.setWidth("70");
        numericalSalary.setFont(font);
        numericalSalary.setFontSize(fontSize);
        numericalSalary.setBold(trueString);
        numericalSalary.setNumericalValue(String.format("%d", salary));
        numericalSalary.setTabId(salaryString);
        numericalSalary.setTabLabel(salaryString);

        LocalePolicyTab localePolicyTab = new LocalePolicyTab();
        localePolicyTab.setCultureName("en-US");
        localePolicyTab.setCurrencyCode("usd");
        localePolicyTab.setCurrencyPositiveFormat("csym_1_comma_234_comma_567_period_89");
        localePolicyTab.setCurrencyNegativeFormat("minus_csym_1_comma_234_comma_567_period_89");
        localePolicyTab.setUseLongCurrencyFormat(trueString);
        numericalSalary.setLocalePolicy(localePolicyTab);

        TextCustomField salaryCustomField = new TextCustomField();
        salaryCustomField.setName(salaryString);
        salaryCustomField.setRequired(falseString);
        salaryCustomField.setShow(trueString);
        salaryCustomField.setValue(String.valueOf(salary));
        CustomFields cf = new CustomFields();
        cf.setTextCustomFields(Collections.singletonList(salaryCustomField));

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Collections.singletonList(signHere));
        tabs.setTextTabs(Arrays.asList(textLegal, textFamiliar));
        tabs.setNumericalTabs(List.of(numericalSalary));

        signer.setTabs(tabs);
        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();
        expectedEnvelopeDefinition.setEmailSubject(emailSubject);
        expectedEnvelopeDefinition.setRecipients(recipients);
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, initialId);
        expectedEnvelopeDefinition.setDocuments(Collections.singletonList(doc));
        expectedEnvelopeDefinition.setCustomFields(cf);
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        // Act
        EnvelopeDefinition envelopeDefinition = SetTabValuesService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName());

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void SetEmailAndSignerNameForRecipientViewRequest_CorrectInputValues_RecipientViewRequest() {
        //Arrange
        RecipientViewRequest expectedViewRequest = new RecipientViewRequest();
        String authenticationMethod = "none";
        String pingFrequency = "600";
        expectedViewRequest.setAuthenticationMethod(authenticationMethod);
        expectedViewRequest.setEmail(testConfig.getSignerEmail());
        expectedViewRequest.setUserName(testConfig.getSignerName());
        expectedViewRequest.setClientUserId(SIGNER_CLIENT_ID);
        expectedViewRequest.setPingFrequency(pingFrequency);
        expectedViewRequest.setPingUrl(RedirectUrl);

        // Act
        RecipientViewRequest viewRequest = SetTabValuesService.setEmailAndSignerNameForRecipientViewRequest(
                new RecipientViewRequest(),
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                RedirectUrl);

        // Assert
        Assertions.assertNotNull(viewRequest);
        Assertions.assertEquals(expectedViewRequest, viewRequest);
    }

    @Test
    public void MakeRecipientViewRequest_CorrectInputValues_RecipientViewRequest() {
        //Arrange
        RecipientViewRequest expectedViewRequest = new RecipientViewRequest();
        String state = "?state=123";
        String pingFrequency = "600";
        String authenticationMethod = "none";

        expectedViewRequest.setReturnUrl(RedirectUrl + state);
        expectedViewRequest.setAuthenticationMethod(authenticationMethod);
        expectedViewRequest.setEmail(testConfig.getSignerEmail());
        expectedViewRequest.setUserName(testConfig.getSignerName());
        expectedViewRequest.setClientUserId(SIGNER_CLIENT_ID);
        expectedViewRequest.setPingFrequency(pingFrequency);
        expectedViewRequest.setPingUrl(RedirectUrl);

        // Act
        RecipientViewRequest viewRequest = SetTabValuesService.makeRecipientViewRequest(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                RedirectUrl,
                RedirectUrl);

        // Assert
        Assertions.assertNotNull(viewRequest);
        Assertions.assertEquals(expectedViewRequest, viewRequest);
    }

    @Test
    public void CreateEnvelope_CorrectInputValues_EnvelopeSummary() throws IOException, ApiException {
        //Arrange
        EnvelopeDefinition envelope = SetTabValuesService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName());

        // Act
        EnvelopeSummary envelopeSummary = SetTabValuesService.createEnvelope(
                envelopesApi,
                testConfig.getAccountId(),
                envelope);

        // Assert
        Assertions.assertNotNull(envelopeSummary);
        Assertions.assertNotNull(envelopeSummary.getEnvelopeId());
    }

    @Test
    public void CreateRecipientView_CorrectInputValues_ViewUrl() throws IOException, ApiException {
        //Arrange
        EnvelopeDefinition envelope = SetTabValuesService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName());

        EnvelopeSummary envelopeSummary = SetTabValuesService.createEnvelope(
                envelopesApi,
                testConfig.getAccountId(),
                envelope);

        RecipientViewRequest viewRequest = SetTabValuesService.makeRecipientViewRequest(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                RedirectUrl,
                RedirectUrl);

        // Act
        ViewUrl viewUrl = SetTabValuesService.createRecipientView(
                envelopesApi,
                testConfig.getAccountId(),
                envelopeSummary.getEnvelopeId(),
                viewRequest);

        // Assert
        Assertions.assertNotNull(viewUrl);
    }
}

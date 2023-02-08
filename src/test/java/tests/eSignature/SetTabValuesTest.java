package tests.eSignature;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.SetTabValuesService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public final class SetTabValuesTest {
    private final String RedirectUrl = "https://developers.docusign.com/platform/auth/consent";
    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_salary.docx";
    private static final String DOCUMENT_NAME = "Lorem Ipsum";
    private static final String SIGNER_CLIENT_ID = "1000";
    private TestConfig testConfig;
    private static String basePathAddition = "/restapi";
    private final ApiClient apiClient;
    private final EnvelopesApi envelopesApi;

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
        String trueString = "true";
        String recipientId = "1";
        String anchorUnits = "pixels";
        String font = "helvetica";
        Integer salary = 123000;
        String falseString = "false";
        String salaryString = "salary";

        Signer signer = new Signer();
        signer.setEmail(testConfig.getSignerEmail());
        signer.setName(testConfig.getSignerName());
        signer.clientUserId(SIGNER_CLIENT_ID);
        signer.recipientId(recipientId);

        SignHere signHere = new SignHere();
        signHere.setAnchorString("/sn1");
        signHere.setAnchorUnits(anchorUnits);
        signHere.setAnchorYOffset("10");
        signHere.setAnchorXOffset("20");

        Text textLegal = new Text();
        textLegal.setAnchorString("/legal/");
        textLegal.setAnchorUnits(anchorUnits);
        textLegal.setAnchorYOffset("-9");
        textLegal.setAnchorXOffset("5");
        textLegal.setFont(font);
        textLegal.setFontSize("size11");
        textLegal.setBold(trueString);
        textLegal.setValue(testConfig.getSignerName());
        textLegal.setLocked(falseString);
        textLegal.setTabId("legal_name");
        textLegal.setTabLabel("Legal Name");

        Text textFamiliar = new Text();
        textFamiliar.setAnchorString("/familar/");
        textFamiliar.setAnchorUnits(anchorUnits);
        textFamiliar.setAnchorYOffset("-9");
        textFamiliar.setAnchorXOffset("5");
        textFamiliar.setFont(font);
        textFamiliar.setFontSize("size11");
        textFamiliar.setBold(trueString);
        textFamiliar.setValue(testConfig.getSignerName());
        textFamiliar.setLocked(falseString);
        textFamiliar.setTabId("familiar_name");
        textFamiliar.setTabLabel("Familiar Name");

        Text textSalary = new Text();
        textSalary.setAnchorString("/salary/");
        textSalary.setAnchorUnits(anchorUnits);
        textSalary.setAnchorYOffset("-9");
        textSalary.setAnchorXOffset("5");
        textSalary.setFont(font);
        textSalary.setFontSize("size11");
        textSalary.setBold(trueString);
        textSalary.setValue(String.format("$ %d", salary));
        textSalary.setLocked(trueString);
        textSalary.setTabId(salaryString);
        textSalary.setTabLabel(salaryString);

        TextCustomField salaryCustomField = new TextCustomField();
        salaryCustomField.setName(salaryString);
        salaryCustomField.setRequired(falseString);
        salaryCustomField.setShow(trueString);
        salaryCustomField.setValue(String.valueOf(salary));

        CustomFields cf = new CustomFields();
        cf.setTextCustomFields(Collections.singletonList(salaryCustomField));

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Collections.singletonList(signHere));
        tabs.setTextTabs(Arrays.asList(textLegal, textFamiliar, textSalary));
        signer.setTabs(tabs);
        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();
        expectedEnvelopeDefinition.setEmailSubject(emailSubject);
        expectedEnvelopeDefinition.setRecipients(recipients);
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "3");
        expectedEnvelopeDefinition.setDocuments(Collections.singletonList(doc));
        expectedEnvelopeDefinition.setCustomFields(cf);
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        // Act
        EnvelopeDefinition envelopeDefinition = SetTabValuesService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName());

        // Assert
        Assert.assertNotNull(envelopeDefinition);
        Assert.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
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
        Assert.assertNotNull(viewRequest);
        Assert.assertEquals(expectedViewRequest, viewRequest);
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
        Assert.assertNotNull(viewRequest);
        Assert.assertEquals(expectedViewRequest, viewRequest);
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
        Assert.assertNotNull(envelopeSummary);
        Assert.assertNotNull(envelopeSummary.getEnvelopeId());
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
        Assert.assertNotNull(viewUrl);
    }
}

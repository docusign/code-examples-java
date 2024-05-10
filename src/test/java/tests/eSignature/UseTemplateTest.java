package tests.eSignature;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.UseTemplateService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;
import java.util.Arrays;

public final class UseTemplateTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final EnvelopesApi envelopesApi;

    private final TestConfig testConfig;

    public UseTemplateTest() throws IOException, ApiException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.envelopesApi = new EnvelopesApi(apiClient);

        CreateNewTemplateTest createNewTemplateTest = new CreateNewTemplateTest();
        createNewTemplateTest.CreateTemplate_CorrectInputValues_TemplateSummary();
    }

    @Test
    public void ListTemplates_CorrectInputValues_EnvelopeTemplateResults() throws ApiException {
        // Act
        EnvelopeTemplateResults templates = UseTemplateService.listTemplates(apiClient, testConfig.getAccountId());

        // Assert
        Assertions.assertNotNull(templates);
    }

    @Test
    public void MakeEnvelopeFromTemplate_CorrectInputValues_EnvelopeDefinition() {
        // Arrange
        String ccEmail = "cc@gmail.com";
        String ccName = "cc";
        TemplateRole signer = new TemplateRole();
        signer.setEmail(testConfig.getSignerEmail());
        signer.setName(testConfig.getSignerName());
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole();
        cc.setEmail(ccEmail);
        cc.setName(ccName);
        cc.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);

        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();
        expectedEnvelopeDefinition.setTemplateId(testConfig.getTemplateId());
        expectedEnvelopeDefinition.setTemplateRoles(Arrays.asList(signer, cc));
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        // Act
        EnvelopeDefinition envelopeDefinition = UseTemplateService.makeEnvelope(
                testConfig.getSignerName(),
                testConfig.getSignerEmail(),
                ccEmail,
                ccName,
                testConfig.getTemplateId()
        );

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void CreateEnvelopeTemplate_CorrectInputValues_EnvelopeSummary() throws ApiException {
        // Arrange
        String ccEmail = "cc@gmail.com";
        String ccName = "cc";

        EnvelopeDefinition envelopeDefinition = UseTemplateService.makeEnvelope(
                testConfig.getSignerName(),
                testConfig.getSignerEmail(),
                ccEmail,
                ccName,
                testConfig.getTemplateId()
        );

        // Act
        EnvelopeSummary envelopeSummary = UseTemplateService.createEnvelopeTemplate(
                envelopesApi,
                testConfig.getAccountId(),
                envelopeDefinition);

        // Assert
        Assertions.assertNotNull(envelopeSummary);
        Assertions.assertNotNull(envelopeSummary.getEnvelopeId());
    }
}

package tests.eSignature;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.ApplyBrandToEnvelopeService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.AccountsApi;
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
import java.util.Collections;
import java.util.List;

public final class ApplyBrandToEnvelopeTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "EG025 Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final AccountsApi accountsApi;

    private final EnvelopesApi envelopesApi;

    private final TestConfig testConfig;

    public ApplyBrandToEnvelopeTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());

        this.accountsApi = new AccountsApi(apiClient);
        this.envelopesApi = new EnvelopesApi(apiClient);
    }

    @Test
    public void GetBrands_CorrectInputValues_ReturnsBrandsResponse() throws ApiException {
        // Act
        BrandsResponse brand = ApplyBrandToEnvelopeService.getBrands(
                this.accountsApi,
                testConfig.getAccountId());

        testConfig.setBrand(brand.getBrands().stream().findFirst().get());

        // Assert
        Assertions.assertNotNull(brand);
        Assertions.assertNotNull(brand.getBrands());
    }

    @Test
    public void MakeEnvelope_CorrectInputValues_ReturnsEnvelopeDefinition() throws IOException {
        // Arrange
        String defaultID = "1";
        String emailSubject = "EG029. Please Sign";
        String anchorString = "/sn1/";
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, defaultID);

        Tabs tabs = EnvelopeHelpers.createSingleSignerTab(anchorString, ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        Signer signer = new Signer()
                .email(testConfig.getSignerEmail())
                .name(testConfig.getSignerName())
                .recipientId(defaultID)
                .routingOrder(defaultID)
                .tabs(tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition()
                .emailSubject(emailSubject)
                .documents(List.of(document))
                .recipients(recipients)
                .brandId(testConfig.getBrand().getBrandId())
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        // Act
        EnvelopeDefinition envelopeDefinition = ApplyBrandToEnvelopeService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                testConfig.getBrand().getBrandId());

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void ApplyBrandToEnvelope_CorrectInputValues_ReturnsEnvelopeSummary() throws ApiException, IOException {
        // Arrange
        EnvelopeDefinition envelopeDefinition = ApplyBrandToEnvelopeService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                testConfig.getBrand().getBrandId());

        // Act
        EnvelopeSummary envelopeSummary = ApplyBrandToEnvelopeService.applyBrandToEnvelope(
                this.envelopesApi,
                testConfig.getAccountId(),
                envelopeDefinition);

        // Assert
        Assertions.assertNotNull(envelopeSummary);
    }
}

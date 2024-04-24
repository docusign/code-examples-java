package tests.eSignature;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.EmbeddedSigningService;
import com.docusign.core.model.ApiType;
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

public final class EmbeddedSigningTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = 20;

    private static final int ANCHOR_OFFSET_X = 10;

    private static final String SIGNER_CLIENT_ID = "1000";

    private static final String basePathAddition = "/restapi";

    private final String RedirectUrl = "https://developers.docusign.com/platform/auth/consent";

    private final ApiClient apiClient;

    private final TestConfig testConfig;

    public EmbeddedSigningTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
    }

    @Test
    public void MakeEnvelope_CorrectInputValues_EnvelopeDefinition() throws IOException {
        // Arrange
        String anchorString = "/sn1/";
        String emailSubject = "Please sign this document";
        String docId = "3";
        String recipientId = "1";
        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();

        Signer signer = new Signer();
        signer.setEmail(testConfig.getSignerEmail());
        signer.setName(testConfig.getSignerName());
        signer.clientUserId(SIGNER_CLIENT_ID);
        signer.recipientId(recipientId);
        signer.setTabs(EnvelopeHelpers.createSingleSignerTab(anchorString, ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));
        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        expectedEnvelopeDefinition.setEmailSubject(emailSubject);
        expectedEnvelopeDefinition.setRecipients(recipients);
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, docId);
        expectedEnvelopeDefinition.setDocuments(Collections.singletonList(doc));
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        // Act
        EnvelopeDefinition envelopeDefinition = EmbeddedSigningService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void MakeRecipientViewRequest_CorrectInputValues_RecipientViewRequest() {
        //Arrange
        RecipientViewRequest expectedViewRequest = new RecipientViewRequest();
        String stateValue = "?state=123";
        String authenticationMethod = "none";
        String pingFrequency = "600";

        expectedViewRequest.setReturnUrl(RedirectUrl + stateValue);
        expectedViewRequest.setAuthenticationMethod(authenticationMethod);
        expectedViewRequest.setEmail(testConfig.getSignerEmail());
        expectedViewRequest.setUserName(testConfig.getSignerName());
        expectedViewRequest.setClientUserId(SIGNER_CLIENT_ID);
        expectedViewRequest.setPingFrequency(pingFrequency);
        expectedViewRequest.setPingUrl(RedirectUrl);

        // Act
        RecipientViewRequest viewRequest = EmbeddedSigningService.makeRecipientViewRequest(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                RedirectUrl,
                RedirectUrl);

        // Assert
        Assertions.assertNotNull(viewRequest);
        Assertions.assertEquals(expectedViewRequest, viewRequest);
    }

    @Test
    public void CreateEnvelope_CorrectInputValues_EnvelopeId() throws IOException, ApiException {
        //Arrange
        EnvelopeDefinition envelope = EmbeddedSigningService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        // Act
        String envelopeId = EmbeddedSigningService.createEnvelope(apiClient, testConfig.getAccountId(), envelope);

        // Assert
        Assertions.assertNotNull(envelopeId);
    }

    @Test
    public void EmbeddedSigning_CorrectInputValues_ViewUrl() throws IOException, ApiException {
        //Arrange
        EnvelopeDefinition envelope = EmbeddedSigningService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        String envelopeId = EmbeddedSigningService.createEnvelope(apiClient, testConfig.getAccountId(), envelope);

        RecipientViewRequest viewRequest = EmbeddedSigningService.makeRecipientViewRequest(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                RedirectUrl,
                RedirectUrl);

        // Act
        ViewUrl viewUrl = EmbeddedSigningService.embeddedSigning(
                apiClient,
                testConfig.getAccountId(),
                envelopeId,
                viewRequest
        );

        // Assert
        Assertions.assertNotNull(viewUrl);
    }
}

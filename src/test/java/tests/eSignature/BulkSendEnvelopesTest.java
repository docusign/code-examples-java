package tests.eSignature;

import com.docusign.controller.eSignature.examples.DsModelUtils;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.BulkSendEnvelopesService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.BulkEnvelopesApi;
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

public final class BulkSendEnvelopesTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final long BULK_REQUEST_DELAY = 15L;

    private static final String BULK_SIGNER_EMAIL_PLACEHOLDER = "MultiBulkRecipients-%s@docusign.com";

    private static final String BULK_SIGNER_NAME_PLACEHOLDER = "Multi Bulk Recipients::%s";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = -5;

    private static final int ANCHOR_OFFSET_X = 15;

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final BulkEnvelopesApi bulkEnvelopesApi;

    private final TestConfig testConfig;

    public BulkSendEnvelopesTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());

        this.bulkEnvelopesApi = new BulkEnvelopesApi(apiClient);
    }

    @Test
    public void CreateCCPlaceholder_CorrectInputValues_ReturnsCarbonCopy() {
        // Arrange
        String roleName = "cc";
        String routingOrder = "1";
        String recipientId = "1";
        CarbonCopy expectedCarbonCopy = new CarbonCopy()
                .name(String.format(BULK_SIGNER_NAME_PLACEHOLDER, roleName))
                .email(String.format(BULK_SIGNER_EMAIL_PLACEHOLDER, roleName))
                .roleName(roleName)
                .note("")
                .routingOrder(routingOrder)
                .status(EnvelopeHelpers.SIGNER_STATUS_CREATED)
                .deliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL)
                .recipientId(recipientId)
                .recipientType(roleName);

        // Act
        CarbonCopy carbonCopy = BulkSendEnvelopesService.createCCPlaceholder(
                roleName,
                recipientId,
                routingOrder);

        // Assert
        Assertions.assertNotNull(carbonCopy);
        Assertions.assertEquals(expectedCarbonCopy, carbonCopy);
    }

    @Test
    public void CreateSignerPlaceholder_CorrectInputValues_ReturnsSigner() {
        // Arrange
        String roleName = "signer";
        String routingOrder = "1";
        String recipientId = "1";
        String anchorString = "/sn1/";
        Signer expectedSigner = new Signer()
                .name(String.format(BULK_SIGNER_NAME_PLACEHOLDER, roleName))
                .email(String.format(BULK_SIGNER_EMAIL_PLACEHOLDER, roleName))
                .roleName(roleName)
                .note("")
                .routingOrder(routingOrder)
                .status(EnvelopeHelpers.SIGNER_STATUS_CREATED)
                .deliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL)
                .recipientId(recipientId)
                .recipientType(roleName)
                .tabs(EnvelopeHelpers.createSingleSignerTab(anchorString, ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        // Act
        Signer signer = BulkSendEnvelopesService.createSignerPlaceholder(
                roleName,
                recipientId,
                routingOrder);

        // Assert
        Assertions.assertNotNull(signer);
        Assertions.assertEquals(expectedSigner, signer);
    }

    @Test
    public void CreateRecipients_CorrectInputValues_ReturnsRecipients() {
        // Arrange
        String defaultId = "1";
        String defaultIdTwo = "2";
        Signer signer = BulkSendEnvelopesService.createSignerPlaceholder(EnvelopeHelpers.SIGNER_ROLE_NAME, defaultId, defaultId);
        CarbonCopy carbonCopy = BulkSendEnvelopesService.createCCPlaceholder(EnvelopeHelpers.CC_ROLE_NAME, defaultIdTwo, defaultIdTwo);
        Recipients expectedRecipients = new Recipients()
                .signers(List.of(signer))
                .carbonCopies(List.of(carbonCopy));

        // Act
        Recipients recipients = BulkSendEnvelopesService.createRecipients();

        // Assert
        Assertions.assertNotNull(recipients);
        Assertions.assertEquals(expectedRecipients, recipients);
    }

    @Test
    public void CreateCustomFields_CorrectInputValues_ReturnsCustomFields() {
        // Arrange
        String bulkListId = "1";
        String mailingListId = "mailingListId";
        TextCustomField textCustomField = new TextCustomField()
                .name(mailingListId)
                .required(DsModelUtils.FALSE)
                .show(DsModelUtils.FALSE)
                .value(bulkListId);

        CustomFields expectedCustomFields = new CustomFields()
                .listCustomFields(Collections.emptyList())
                .textCustomFields(List.of(textCustomField));

        // Act
        CustomFields customFields = BulkSendEnvelopesService.createCustomFields(bulkListId);

        // Assert
        Assertions.assertNotNull(customFields);
        Assertions.assertEquals(expectedCustomFields, customFields);
    }

    @Test
    public void MakeEnvelope_CorrectInputValues_ReturnsEnvelopeDefinition() throws IOException {
        // Arrange
        String docId = "1";
        String emailSubject = "EG031 Please sign";
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, docId);
        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition()
                .documents(List.of(document))
                .envelopeIdStamping(DsModelUtils.TRUE)
                .emailSubject(emailSubject)
                .status(EnvelopeHelpers.ENVELOPE_STATUS_CREATED)
                .recipients(BulkSendEnvelopesService.createRecipients());

        // Act
        EnvelopeDefinition envelopeDefinition = BulkSendEnvelopesService.makeEnvelope();

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void CreateBulkSending_CorrectInputValues_ReturnsBulkSendingCopy() {
        // Arrange
        String ccName = "CC";
        String ccEmail = "cc@gmail.com";

        BulkSendingCopyRecipient recipient1 = new BulkSendingCopyRecipient()
                .name(testConfig.getSignerName())
                .email(testConfig.getSignerEmail())
                .tabs(Collections.emptyList())
                .roleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        BulkSendingCopyRecipient recipient2 = new BulkSendingCopyRecipient()
                .name(ccName)
                .email(ccEmail)
                .tabs(Collections.emptyList())
                .roleName(EnvelopeHelpers.CC_ROLE_NAME);

        BulkSendingCopy expectedBulkSendingCopy = new BulkSendingCopy()
                .recipients(List.of(recipient1, recipient2))
                .customFields(Collections.emptyList());

        // Act
        BulkSendingCopy bulkSendingCopy = BulkSendEnvelopesService.createBulkSending(
                testConfig.getSignerName(),
                testConfig.getSignerEmail(),
                ccName,
                ccEmail
        );

        // Assert
        Assertions.assertNotNull(bulkSendingCopy);
        Assertions.assertEquals(expectedBulkSendingCopy, bulkSendingCopy);
    }

    @Test
    public void GetSendingList_CorrectInputValues_ReturnsBulkSendingList() {
        // Arrange
        String ccName = "CC";
        String ccEmail = "cc@gmail.com";
        String cc2Name = "CC 2";
        String cc2Email = "cc2@gmail.com";
        String signer2Name = "Signer 2";
        String signer2Email = "signer2@gmail.com";
        String fileName = "sample.csv";

        List<BulkSendingCopy> copies = List.of(
                BulkSendEnvelopesService.createBulkSending(testConfig.getSignerName(), testConfig.getSignerEmail(), ccName, ccEmail),
                BulkSendEnvelopesService.createBulkSending(signer2Name, signer2Email, cc2Name, cc2Email)
        );
        BulkSendingList expectedBulkSendingList = new BulkSendingList()
                .name(fileName)
                .bulkCopies(copies);

        // Act
        BulkSendingList bulkSendingList = BulkSendEnvelopesService.getSendingList(
                testConfig.getSignerName(),
                testConfig.getSignerEmail(),
                ccName,
                ccEmail,
                signer2Name,
                signer2Email,
                cc2Name,
                cc2Email
        );

        // Assert
        Assertions.assertNotNull(bulkSendingList);
        Assertions.assertEquals(expectedBulkSendingList, bulkSendingList);
    }

    @Test
    public void BulkSendEnvelopes_CorrectInputValues_ReturnsString() throws ApiException, IOException {
        // Arrange
        String ccName = "CC";
        String ccEmail = "cc@gmail.com";
        String cc2Name = "CC 2";
        String cc2Email = "cc2@gmail.com";
        String signer2Name = "Signer 2";
        String signer2Email = "signer2@gmail.com";

        // Act
        String batchId = BulkSendEnvelopesService.bulkSendEnvelopes(
                this.bulkEnvelopesApi,
                this.apiClient,
                testConfig.getSignerName(),
                testConfig.getSignerEmail(),
                ccName,
                ccEmail,
                signer2Name,
                signer2Email,
                cc2Name,
                cc2Email,
                testConfig.getAccountId()
        );
        testConfig.setBatchId(batchId);

        // Assert
        Assertions.assertNotNull(batchId);
    }

    @Test
    public void GetBulkSendBatchStatus_CorrectInputValues_ReturnsBulkSendBatchStatus() throws ApiException, IOException, InterruptedException {
        // Arrange
        BulkSendEnvelopes_CorrectInputValues_ReturnsString();

        // Act
        BulkSendBatchStatus bulkSendBatchStatus = BulkSendEnvelopesService.getBulkSendBatchStatus(
                this.bulkEnvelopesApi,
                testConfig.getAccountId(),
                testConfig.getBatchId(),
                BULK_REQUEST_DELAY
        );

        // Assert
        Assertions.assertNotNull(bulkSendBatchStatus);
    }
}

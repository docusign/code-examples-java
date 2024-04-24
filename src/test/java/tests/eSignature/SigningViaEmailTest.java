package tests.eSignature;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.SigningViaEmailService;
import com.docusign.core.common.DocumentType;
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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class SigningViaEmailTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon.ftl";

    private static final String HTML_DOCUMENT_NAME = "Order acknowledgement";

    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";

    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final EnvelopesApi envelopesApi;

    private final TestConfig testConfig;

    public SigningViaEmailTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.envelopesApi = new EnvelopesApi(apiClient);
    }

    @Test
    public void MakeEnvelopeWithCC_CorrectInputValues_EnvelopeDefinition() throws IOException {
        // Arrange
        String ccEmail = "cc@gmail.com";
        String ccName = "cc";
        String anchorStringOne = "**signature_1**";
        String anchorStringTwo = "/sn1/";
        String emailSubject = "Please sign this document set";
        String defaultIdOne = "1";
        String defaultIdTwo = "2";
        String defaultIdThree = "3";
        WorkArguments args = new WorkArguments();
        args.setSignerEmail(testConfig.getSignerEmail());
        args.setSignerName(testConfig.getSignerName());
        args.setCcEmail(ccEmail);
        args.setCcName(ccName);

        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere(anchorStringOne, ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X),
                EnvelopeHelpers.createSignHere(anchorStringTwo, ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));
        Signer signer = new Signer();
        signer.setEmail(testConfig.getSignerEmail());
        signer.setName(testConfig.getSignerName());
        signer.setRecipientId(defaultIdOne);
        signer.setRoutingOrder(defaultIdOne);
        signer.setTabs(signerTabs);

        CarbonCopy cc = new CarbonCopy();
        cc.setEmail(ccEmail);
        cc.setName(ccName);
        cc.setRecipientId(defaultIdTwo);
        cc.setRoutingOrder(defaultIdTwo);

        byte[] htmlDocument = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args)
                .getBytes(StandardCharsets.UTF_8);
        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();
        expectedEnvelopeDefinition.setEmailSubject(emailSubject);
        expectedEnvelopeDefinition.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocument(htmlDocument, HTML_DOCUMENT_NAME,
                        DocumentType.HTML.getDefaultFileExtention(), defaultIdOne),
                EnvelopeHelpers.createDocumentFromFile(DOCX_DOCUMENT_FILE_NAME, DOCX_DOCUMENT_NAME, defaultIdTwo),
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, defaultIdThree)));
        expectedEnvelopeDefinition.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        // Act
        EnvelopeDefinition envelopeDefinition = SigningViaEmailService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                ccEmail,
                ccName,
                EnvelopeHelpers.ENVELOPE_STATUS_SENT,
                args
        );

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void SigningViaEmail_CorrectInputValues_EnvelopeSummary() throws IOException, ApiException {
        //Arrange
        String ccEmail = "cc@gmail.com";
        String ccName = "cc";
        WorkArguments args = new WorkArguments();
        args.setSignerEmail(testConfig.getSignerEmail());
        args.setSignerName(testConfig.getSignerName());
        args.setCcEmail(ccEmail);
        args.setCcName(ccName);

        EnvelopeDefinition envelope = SigningViaEmailService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                ccEmail,
                ccName,
                EnvelopeHelpers.ENVELOPE_STATUS_SENT,
                args
        );

        // Act
        EnvelopeSummary envelopeSummary = SigningViaEmailService.signingViaEmail(
                envelopesApi,
                testConfig.getAccountId(),
                envelope);

        // Assert
        Assertions.assertNotNull(envelopeSummary);
    }
}

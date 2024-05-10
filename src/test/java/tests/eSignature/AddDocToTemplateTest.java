package tests.eSignature;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.controller.eSignature.services.AddDocToTemplateService;
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
import java.util.Collections;

public final class AddDocToTemplateTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon2.ftl";

    private static final String HTML_DOCUMENT_NAME = "Appendix 1--Sales order";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    private static final String basePathAddition = "/restapi";

    private final String SIGNER_CLIENT_ID = "1000";

    private final String RedirectUrl = "https://developers.docusign.com/platform/auth/consent";

    private final ApiClient apiClient;

    private final EnvelopesApi envelopesApi;

    private final TestConfig testConfig;

    public AddDocToTemplateTest() throws IOException, ApiException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.envelopesApi = new EnvelopesApi(apiClient);

        CreateNewTemplateTest createNewTemplateTest = new CreateNewTemplateTest();
        createNewTemplateTest.CreateTemplate_CorrectInputValues_TemplateSummary();
    }

    @Test
    public void AddDocumentToTemplate_CorrectInputValues_ViewUrl() throws ApiException, IOException {
        // Arrange
        String ccEmail = "cc@gmail.com";
        String ccName = "cc";
        String item = "kiwi";
        String quantity = "1";

        WorkArguments args = new WorkArguments();
        args.setSignerEmail(testConfig.getSignerEmail());
        args.setSignerName(testConfig.getSignerName());
        args.setCcEmail(ccEmail);
        args.setCcName(ccName);
        args.setItem(item);
        args.setQuantity(quantity);

        // Act
        ViewUrl embeddedEnvelope = AddDocToTemplateService.addDocumentToTemplate(
                envelopesApi,
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                ccEmail,
                ccName,
                testConfig.getTemplateId(),
                testConfig.getAccountId(),
                RedirectUrl,
                RedirectUrl,
                args
        );

        // Assert
        Assertions.assertNotNull(embeddedEnvelope);
    }

    @Test
    public void MakeRecipientViewRequest_CorrectInputValues_RecipientViewRequest() {
        // Arrange
        String pingFrequency = "600";
        String authenticationMethod = "none";

        RecipientViewRequest expectedViewRequest = new RecipientViewRequest();
        expectedViewRequest.setReturnUrl(RedirectUrl);
        expectedViewRequest.setAuthenticationMethod(authenticationMethod);
        expectedViewRequest.setEmail(testConfig.getSignerEmail());
        expectedViewRequest.setUserName(testConfig.getSignerName());
        expectedViewRequest.setClientUserId(SIGNER_CLIENT_ID);
        expectedViewRequest.setPingFrequency(pingFrequency);
        expectedViewRequest.setPingUrl(RedirectUrl);

        // Act
        RecipientViewRequest recipientViewRequest = AddDocToTemplateService.makeRecipientViewRequest(
                RedirectUrl,
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                RedirectUrl
        );

        // Assert
        Assertions.assertNotNull(recipientViewRequest);
        Assertions.assertEquals(expectedViewRequest, recipientViewRequest);
    }

    @Test
    public void MakeEnvelope_CorrectInputValues_EnvelopeDefinition() throws IOException {
        // Arrange
        String ccEmail = "cc@gmail.com";
        String ccName = "cc";
        String item = "kiwi";
        String defaultIdOne = "1";
        String defaultIdTwo = "2";
        String anchorString = "**signature_1**";

        WorkArguments args = new WorkArguments();
        args.setSignerEmail(testConfig.getSignerEmail());
        args.setSignerName(testConfig.getSignerName());
        args.setCcEmail(ccEmail);
        args.setCcName(ccName);
        args.setItem(item);
        args.setQuantity(defaultIdOne);

        CarbonCopy cc = new CarbonCopy();
        cc.setEmail(ccEmail);
        cc.setName(ccName);
        cc.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc.setRecipientId(defaultIdTwo);

        CompositeTemplate compositeTemplate = new CompositeTemplate();
        compositeTemplate.setCompositeTemplateId(defaultIdOne);
        ServerTemplate serverTemplates = new ServerTemplate();
        serverTemplates.setSequence(defaultIdOne);
        serverTemplates.setTemplateId(testConfig.getTemplateId());
        compositeTemplate.setServerTemplates(Collections.singletonList(serverTemplates));

        InlineTemplate inlineTemplate = new InlineTemplate();
        inlineTemplate.setSequence(defaultIdTwo);
        inlineTemplate.setRecipients(EnvelopeHelpers.createRecipients(
                AddDocToTemplateService.createSigner(
                        testConfig.getSignerEmail(),
                        testConfig.getSignerName(),
                        SIGNER_CLIENT_ID
                ),
                cc));
        compositeTemplate.setInlineTemplates(Collections.singletonList(inlineTemplate));

        Tabs signer1Tabs = EnvelopeHelpers.createSingleSignerTab(
                anchorString,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X);
        Signer signer1AddedDoc = AddDocToTemplateService.createSigner(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID
        );
        signer1AddedDoc.setTabs(signer1Tabs);

        byte[] htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args)
                .getBytes(StandardCharsets.UTF_8);

        CompositeTemplate compTemplate2 = new CompositeTemplate();
        compTemplate2.setCompositeTemplateId(defaultIdTwo);
        InlineTemplate inlineTemplate2 = new InlineTemplate();
        inlineTemplate2.setSequence(defaultIdOne);
        inlineTemplate2.setRecipients(EnvelopeHelpers.createRecipients(signer1AddedDoc, cc));
        compTemplate2.setInlineTemplates(Collections.singletonList(inlineTemplate2));
        compTemplate2.setDocument(EnvelopeHelpers.createDocument(htmlDoc, HTML_DOCUMENT_NAME,
                DocumentType.HTML.getDefaultFileExtention(), defaultIdOne));

        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        expectedEnvelopeDefinition.setCompositeTemplates(Arrays.asList(compositeTemplate, compTemplate2));

        // Act
        EnvelopeDefinition envelopeDefinition = AddDocToTemplateService.makeEnvelope(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID,
                ccEmail,
                ccName,
                testConfig.getTemplateId(),
                args
        );

        // Assert
        Assertions.assertNotNull(envelopeDefinition);
        Assertions.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
    }

    @Test
    public void CreateSigner_CorrectInputValues_Signer() {
        // Arrange
        String recipientId = "1";
        Signer expectedSigner = new Signer();
        expectedSigner.setEmail(testConfig.getSignerEmail());
        expectedSigner.setName(testConfig.getSignerName());
        expectedSigner.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        expectedSigner.setRecipientId(recipientId);
        expectedSigner.setClientUserId(SIGNER_CLIENT_ID);

        // Act
        Signer signer = AddDocToTemplateService.createSigner(
                testConfig.getSignerEmail(),
                testConfig.getSignerName(),
                SIGNER_CLIENT_ID
        );

        // Assert
        Assertions.assertNotNull(signer);
        Assertions.assertEquals(expectedSigner, signer);
    }
}

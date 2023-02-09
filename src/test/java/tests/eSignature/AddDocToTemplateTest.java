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
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public final class AddDocToTemplateTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private final String SIGNER_CLIENT_ID = "1000";
    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon2.ftl";
    private static final String HTML_DOCUMENT_NAME = "Appendix 1--Sales order";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;
    private final String RedirectUrl = "https://developers.docusign.com/platform/auth/consent";
    private TestConfig testConfig;
    private static String basePathAddition = "/restapi";
    private final ApiClient apiClient;
    private final EnvelopesApi envelopesApi;

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
        Assert.assertNotNull(embeddedEnvelope);
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
        Assert.assertNotNull(recipientViewRequest);
        Assert.assertEquals(expectedViewRequest, recipientViewRequest);
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

        CarbonCopy cc1 = new CarbonCopy();
        cc1.setEmail(ccEmail);
        cc1.setName(ccName);
        cc1.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc1.setRecipientId(defaultIdTwo);

        CompositeTemplate compTemplate1 = new CompositeTemplate();
        compTemplate1.setCompositeTemplateId(defaultIdOne);
        ServerTemplate serverTemplates = new ServerTemplate();
        serverTemplates.setSequence(defaultIdOne);
        serverTemplates.setTemplateId(testConfig.getTemplateId());
        compTemplate1.setServerTemplates(Collections.singletonList(serverTemplates));

        InlineTemplate inlineTemplate = new InlineTemplate();
        inlineTemplate.setSequence(defaultIdTwo);
        inlineTemplate.setRecipients(EnvelopeHelpers.createRecipients(
                AddDocToTemplateService.createSigner(
                        testConfig.getSignerEmail(),
                        testConfig.getSignerName(),
                        SIGNER_CLIENT_ID
                ),
                cc1));
        compTemplate1.setInlineTemplates(Collections.singletonList(inlineTemplate));

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
        inlineTemplate2.setRecipients(EnvelopeHelpers.createRecipients(signer1AddedDoc, cc1));
        compTemplate2.setInlineTemplates(Collections.singletonList(inlineTemplate2));
        compTemplate2.setDocument(EnvelopeHelpers.createDocument(htmlDoc, HTML_DOCUMENT_NAME,
                DocumentType.HTML.getDefaultFileExtention(), defaultIdOne));

        EnvelopeDefinition expectedEnvelopeDefinition = new EnvelopeDefinition();
        expectedEnvelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        expectedEnvelopeDefinition.setCompositeTemplates(Arrays.asList(compTemplate1, compTemplate2));

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
        Assert.assertNotNull(envelopeDefinition);
        Assert.assertEquals(expectedEnvelopeDefinition, envelopeDefinition);
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
        Assert.assertNotNull(signer);
        Assert.assertEquals(expectedSigner, signer);
    }
}

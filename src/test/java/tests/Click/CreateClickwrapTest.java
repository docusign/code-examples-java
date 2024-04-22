package tests.Click;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiClient;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.DisplaySettings;
import com.docusign.click.model.Document;
import com.docusign.controller.click.examples.ClickwrapHelper;
import com.docusign.controller.click.services.CreateClickwrapService;
import com.docusign.core.model.ApiType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class CreateClickwrapTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String DOCUMENT_FILE_NAME = "TermsOfService.pdf";

    private static final Integer DOCUMENT_ORDER = 0;

    private static final String DOCUMENT_NAME = "Terms of Service";

    private static final String basePathAddition = "/clickapi";

    private final ApiClient apiClient;

    private final AccountsApi accountsApi;

    private final TestConfig testConfig;

    public CreateClickwrapTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.CLICK);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.accountsApi = new AccountsApi(apiClient);
    }

    @Test
    public void CreateClickwrapRequest_CorrectInputValues_ReturnsClickwrapRequest() throws IOException {
        // Arrange
        String clickwrapName = "Clickwrap name";
        String displayName = "Terms of Service";
        String consentButtonText = "I Agree";
        String format = "modal";
        String documentDisplay = "document";

        Document document = ClickwrapHelper.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ORDER);
        DisplaySettings displaySettings = new DisplaySettings()
                .displayName(displayName)
                .consentButtonText(consentButtonText)
                .downloadable(true)
                .format(format)
                .mustRead(true)
                .requireAccept(true)
                .documentDisplay(documentDisplay);

        ClickwrapRequest expectedClickwrapRequest = new ClickwrapRequest()
                .addDocumentsItem(document)
                .clickwrapName(clickwrapName)
                .requireReacceptance(true)
                .displaySettings(displaySettings);

        // Act
        ClickwrapRequest clickwrapRequest = CreateClickwrapService.createClickwrapRequest(
                clickwrapName,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME,
                DOCUMENT_ORDER);

        // Assert
        Assertions.assertNotNull(clickwrapRequest);
        Assertions.assertEquals(expectedClickwrapRequest, clickwrapRequest);
    }

    @Test
    public void CreateClickwrap_CorrectInputValues_ReturnsClickwrapVersionSummaryResponse() throws ApiException, IOException {
        // Arrange
        byte[] byteArray = new byte[8];
        new Random().nextBytes(byteArray);
        String clickwrapName = new String(byteArray, StandardCharsets.UTF_8);

        ClickwrapRequest clickwrapRequest = CreateClickwrapService.createClickwrapRequest(
                clickwrapName,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME,
                DOCUMENT_ORDER);

        // Act
        ClickwrapVersionSummaryResponse clickwrapVersionSummaryResponse = CreateClickwrapService.createClickwrap(
                this.accountsApi,
                testConfig.getAccountId(),
                clickwrapRequest);

        testConfig.setInactiveClickwrap(clickwrapVersionSummaryResponse);

        // Assert
        Assertions.assertNotNull(clickwrapVersionSummaryResponse);
        Assertions.assertEquals(clickwrapName, clickwrapVersionSummaryResponse.getClickwrapName());
    }
}

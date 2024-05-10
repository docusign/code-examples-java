package tests.Click;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiClient;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.controller.click.services.ActivateClickwrapService;
import com.docusign.core.model.ApiType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;

public final class ActivateClickwrapTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String basePathAddition = "/clickapi";

    private final ApiClient apiClient;

    private final AccountsApi accountsApi;

    private final TestConfig testConfig;

    public ActivateClickwrapTest() throws IOException, ApiException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.CLICK);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.accountsApi = new AccountsApi(apiClient);
        CreateClickwrapTest createClickwrapTest = new CreateClickwrapTest();
        createClickwrapTest.CreateClickwrap_CorrectInputValues_ReturnsClickwrapVersionSummaryResponse();
    }

    @Test
    public void ActivateClickwrap_CorrectInputValues_ReturnsClickwrapVersionSummaryResponse() throws ApiException {
        // Arrange
        var statusActive = "active";

        // Act
        ClickwrapVersionSummaryResponse clickwrapVersionSummaryResponse = ActivateClickwrapService.activateClickwrap(
                this.accountsApi,
                testConfig.getAccountId(),
                testConfig.getInactiveClickwrap().getClickwrapId(),
                testConfig.getInactiveClickwrap().getVersionNumber()
        );

        // Assert
        Assertions.assertNotNull(clickwrapVersionSummaryResponse);
        Assertions.assertEquals(statusActive, clickwrapVersionSummaryResponse.getStatus());
    }
}

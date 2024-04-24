package tests.eSignature;

import com.docusign.controller.eSignature.services.PermissionCreateService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.PermissionProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class PermissionCreateTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final AccountsApi accountsApi;

    private final TestConfig testConfig;

    public PermissionCreateTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.accountsApi = new AccountsApi(apiClient);
    }

    @Test
    public void CreateNewProfile_CorrectInputValues_ReturnsPermissionProfile() throws ApiException {
        // Arrange
        byte[] byteArray = new byte[8];
        new Random().nextBytes(byteArray);
        var permissionName = new String(byteArray, StandardCharsets.UTF_8);

        // Act
        PermissionProfile permissionProfile = PermissionCreateService.createNewProfile(
                this.accountsApi,
                testConfig.getAccountId(),
                permissionName);

        // Assert
        Assertions.assertNotNull(permissionProfile);
        Assertions.assertEquals(permissionName, permissionProfile.getPermissionProfileName());
    }
}

package tests.eSignature;

import com.docusign.controller.eSignature.services.CreateBrandService;
import com.docusign.core.model.ApiType;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import tests.common.JWTLoginMethodTest;
import tests.common.TestConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class CreateBrandTest {
    private static final String BEARER_AUTHENTICATION = "Bearer ";

    private static final String basePathAddition = "/restapi";

    private final ApiClient apiClient;

    private final AccountsApi accountsApi;

    private final TestConfig testConfig;

    public CreateBrandTest() throws IOException {
        JWTLoginMethodTest.RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType.ESIGNATURE);
        this.testConfig = TestConfig.getInstance();

        this.apiClient = new ApiClient(testConfig.getBasePath() + basePathAddition);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + testConfig.getAccessToken());
        this.accountsApi = new AccountsApi(apiClient);
    }

    @Test
    public void CreateBrand_CorrectInputValues_ReturnsBrandsResponse() throws ApiException {
        // Arrange
        byte[] array = new byte[8];
        new Random().nextBytes(array);
        var brandName = new String(array, StandardCharsets.UTF_8);
        var defaultBrandLanguage = "en";

        // Act
        BrandsResponse brand = CreateBrandService.createBrand(
                this.accountsApi,
                brandName,
                defaultBrandLanguage,
                testConfig.getAccountId());

        // Assert
        Assertions.assertNotNull(brand);
        Assertions.assertNotNull(brand.getBrands());
    }
}

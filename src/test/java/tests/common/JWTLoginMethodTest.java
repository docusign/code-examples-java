package tests.common;

import com.docusign.core.model.ApiType;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.junit.jupiter.api.Assertions;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class JWTLoginMethodTest {

    private static final String RedirectUrl = "https://developers.docusign.com/platform/auth/consent";

    private static final String CONSENT_REQUIRED_MESSAGE = "Consent required, please provide consent in browser window and then run this app again.";

    private static final String CONSENT_REQUIRED_KEYWORD = "consent_required";

    public static void RequestJWTUserToken_CorrectInputValues_ReturnOAuthToken(ApiType apiType) throws IOException {
        TestConfig testConfig = TestConfig.getInstance();

        try {
            ApiClient apiClient = new ApiClient(testConfig.getHost());
            List<String> scopes = Arrays.asList(apiType.getScopes());

            byte[] privateKeyBytes = Files.exists(Path.of(testConfig.getPrivateKeyPath())) ?
                    Files.readAllBytes(Paths.get(testConfig.getPrivateKeyPath()))
                    : testConfig.getPrivateKey().getBytes();

            OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                    testConfig.getClientId(),
                    testConfig.getImpersonatedUserId(),
                    scopes,
                    privateKeyBytes,
                    3600);
            String accessToken = oAuthToken.getAccessToken();
            OAuth.UserInfo userInfo = apiClient.getUserInfo(accessToken);
            String accountId = userInfo.getAccounts().get(0).getAccountId();

            Assertions.assertNotNull(accessToken);
            Assertions.assertNotNull(accountId);
            testConfig.setAccessToken(accessToken);
            testConfig.setAccountId(accountId);
        } catch (ApiException exp) {
            if (exp.getMessage().contains(CONSENT_REQUIRED_KEYWORD)) {
                try {
                    String scopes = "impersonation%20";
                    for (String scope : apiType.getScopes()) {
                        scopes += scope + "%20";
                    }
                    System.out.println(CONSENT_REQUIRED_MESSAGE);
                    Desktop.getDesktop().browse(new URI(
                            "https://account-d.docusign.com/oauth/auth?response_type=code&scope="
                                    + scopes
                                    + "&client_id="
                                    + testConfig.getClientId()
                                    + "&redirect_uri="
                                    + JWTLoginMethodTest.RedirectUrl));
                } catch (Exception e) {
                    System.out.print("Error!!! ");
                    System.out.print(e.getMessage());
                }
            }
        }
    }
}

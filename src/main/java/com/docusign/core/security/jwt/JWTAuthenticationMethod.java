package com.docusign.core.security.jwt;

import com.docusign.core.security.SecurityHelpers;

import com.docusign.DSConfiguration;
import com.docusign.core.model.Session;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JWTAuthenticationMethod {

        public static final String CONSENT_REQUIRED = "consent_required";

        public static final String REQUEST_CONSENT_LINK = "https://%s/oauth/auth?prompt=login&response_type=code&scope=%s&client_id=%s&redirect_uri=%s";

        public static final String CONSENT_REDIRECT_URL = "/login/oauth2/code/jwt";

        private static final long TOKEN_EXPIRATION_IN_SECONDS = 3600;

        public RedirectView loginUsingJWT(
                        DSConfiguration configuration,
                        Session session,
                        String redirectURL,
                        List<String> scopes) {
                try {
                        ApiClient apiClient = new ApiClient(configuration.getBasePath());
                        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(configuration.getPrivateKeyPath()));

                        OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                                        configuration.getUserId(),
                                        configuration.getImpersonatedUserId(),
                                        scopes,
                                        privateKeyBytes,
                                        TOKEN_EXPIRATION_IN_SECONDS);
                        String accessToken = oAuthToken.getAccessToken();
                        OAuth.UserInfo userInfo = apiClient.getUserInfo(accessToken);
                        String accountId = userInfo.getAccounts().size() > 0
                                        ? userInfo.getAccounts().get(0).getAccountId()
                                        : "";

                        SecurityHelpers.setSpringSecurityAuthentication(scopes, oAuthToken.getAccessToken(),
                                        userInfo,
                                        accountId, session, oAuthToken.getExpiresIn().toString());
                } catch (ApiException | IOException exp) {
                        if (exp.getMessage().contains(CONSENT_REQUIRED)) {
                                String consent_scopes = String.join("%20", scopes) + "%20impersonation";

                                var consent_url = String.format(
                                                REQUEST_CONSENT_LINK,
                                                configuration.getBaseURL(),
                                                consent_scopes,
                                                configuration.getUserId(),
                                                URLEncoder.encode(configuration.getAppUrl() + CONSENT_REDIRECT_URL,
                                                                StandardCharsets.UTF_8));

                                System.err.println("\nC O N S E N T   R E Q U I R E D" +
                                                "\nAsk the user who will be impersonated to run the following URL: " +
                                                "\n" + consent_url +
                                                "\n\nIt will ask the user to login and to approve access by your application."
                                                +
                                                "\nAlternatively, an Administrator can use Organization Administration to"
                                                +
                                                "\npre-approve one or more users.");

                                configuration.setIsConsentRedirectActivated(true);

                                return new RedirectView(consent_url);
                        }
                }

                return new RedirectView(redirectURL);
        }
}

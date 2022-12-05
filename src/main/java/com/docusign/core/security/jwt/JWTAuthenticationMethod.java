package com.docusign.core.security.jwt;

import com.docusign.DSConfiguration;
import com.docusign.core.model.ApiType;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JWTAuthenticationMethod {

    public static final String CONSENT_REQUIRED = "consent_required";

    public static RedirectView loginUsingJWT(
            ApiType apiType,
            String userId,
            String impersonatedUserId,
            String baseURL,
            DSConfiguration configuration,
            String redirectURL) {
        List<String> scopes = Arrays.asList(apiType.getScopes());

        try {
            ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
            apiClient.setOAuthBasePath("account-d.docusign.com");
            byte[] privateKeyBytes = Files.readAllBytes(Paths.get("src/main/resources/private.key"));

            OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                    userId,
                    impersonatedUserId,
                    scopes,
                    privateKeyBytes,
                    3600);
            String accessToken = oAuthToken.getAccessToken();
            OAuth.UserInfo userInfo = apiClient.getUserInfo(accessToken);
            String accountId = userInfo.getAccounts().get(0).getAccountId();

            JWTAuthenticationMethod.setSpringSecurityAuthentication(scopes, oAuthToken, userInfo, accountId);
        }
        catch (ApiException | IOException exp)
        {
            if (exp.getMessage().contains(CONSENT_REQUIRED))
            {
                String consent_scopes = String.join("%20", scopes) + "%20impersonation";

                var consent_url = String.format(
                    "https://%s/oauth/auth?prompt=login&response_type=code&scope=%s" +
                    "&client_id=%s" +
                    "&redirect_uri=%s",
                    baseURL,
                    consent_scopes,
                    userId,
                    "http://localhost:8080/jwt");

                System.err.println("\nC O N S E N T   R E Q U I R E D" +
                    "\nAsk the user who will be impersonated to run the following URL: " +
                    "\n" + consent_url +
                    "\n\nIt will ask the user to login and to approve access by your application." +
                    "\nAlternatively, an Administrator can use Organization Administration to" +
                    "\npre-approve one or more users.");

                configuration.setIsConsentRedirectActivated(true);

                return new RedirectView(consent_url);
            }
        }

        return new RedirectView(redirectURL);
    }

    private static void setSpringSecurityAuthentication(
            List<String> scopes,
            OAuth.OAuthToken oAuthToken,
            OAuth.UserInfo userInfo,
            String accountId) {
        JWTOAuth2User principal = new JWTOAuth2User();
        principal.setAuthorities(scopes);
        principal.setCreated(userInfo.getCreated());
        principal.setName(userInfo.getName());
        principal.setGivenName(userInfo.getGivenName());
        principal.setFamilyName(userInfo.getFamilyName());
        principal.setSub(userInfo.getSub());
        principal.setEmail(userInfo.getEmail());
        principal.setAccounts(userInfo.getAccounts());
        principal.setAccessToken(oAuthToken);

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(principal, null, accountId);

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

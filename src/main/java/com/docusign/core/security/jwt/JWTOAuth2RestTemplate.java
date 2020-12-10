package com.docusign.core.security.jwt;

import com.docusign.core.exception.LauncherException;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.client.auth.OAuth.OAuthToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;


public class JWTOAuth2RestTemplate extends OAuth2RestTemplate {

    private static final Logger logger = LoggerFactory.getLogger(JWTOAuth2RestTemplate.class);
    private static final long TOKEN_EXPIRATION_IN_SECONDS = 3600;

    private final JWTAuthorizationCodeResourceDetails resource;
    private final OAuth2ClientContext context;
    private ApiClient apiClient;

    public JWTOAuth2RestTemplate(JWTAuthorizationCodeResourceDetails resource, OAuth2ClientContext context) {
        super(resource, context);
        this.resource = resource;
        this.context = context;
        apiClient = resource.getApiClient();
    }

    @Override
    public OAuth2AccessToken getAccessToken() throws UserRedirectRequiredException {
        OAuth2AccessToken accessToken = context.getAccessToken();

        if (accessToken == null || accessToken.isExpired()) {
            try {
                accessToken = accessToken();
                context.setAccessToken(accessToken);
            } catch (Exception e) {
                context.setAccessToken(null);
                try {
                    throw e;
                } catch (LauncherException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return accessToken;
    }

    private OAuth2AccessToken accessToken() throws LauncherException {
        logger.info("Fetching an access token via JWT grant...");
// Only signature scope is needed. Impersonation scope is implied.
        List<String> scopes = resource.getScopeByApiName();
        byte[] privateKeyBytes;
        try {
            privateKeyBytes = resource.getRsaBytes();
        } catch (LauncherException e) {
            throw new AccessTokenRequiredException(e.getMessage(), resource);
        }

        apiClient.setOAuthBasePath((String) resource.getBaseUrl());
        OAuthToken oAuthToken;
        try {
            oAuthToken = apiClient.requestJWTUserToken(
                    resource.getClientId(),
                    resource.getImpersonatedUserGuid(),
                    scopes,
                    privateKeyBytes,
                    TOKEN_EXPIRATION_IN_SECONDS);
        } catch (IOException | ApiException e) {
// Special handling for consent_required
            String message = e.getMessage();
            String consent_url = "";
            String consent_scopes = String.join(" ", scopes) + " impersonation";
            if (message != null && message.contains("consent_required")) {
                consent_url = String.format("https://%s/oauth/auth?prompt=login&response_type=code&scope=%s" +
                    "&client_id=%s" +
                    "&redirect_uri=%s",
                    resource.getBaseUrl(),
                    consent_scopes,
                    resource.getClientId(),
                    "http://localhost:8080/login%26type%3Djwt");
                System.err.println("\nC O N S E N T   R E Q U I R E D" +
                        "\nAsk the user who will be impersonated to run the following URL: " +
                        "\n" + consent_url +
                        "\n\nIt will ask the user to login and to approve access by your application." +
                        "\nAlternatively, an Administrator can use Organization Administration to" +
                        "\npre-approve one or more users.");
                        throw new AccessTokenRequiredException(consent_url, resource);
            }

            throw new AccessTokenRequiredException(e.getMessage(), resource);

        }
        apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());
        logger.info("Done. Continuing...");
        return convert(oAuthToken);
    }

    private static OAuth2AccessToken convert(OAuthToken oAuthToken) {
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(oAuthToken.getAccessToken());
        accessToken.setExpiration(new Date(System.currentTimeMillis() + (oAuthToken.getExpiresIn() * 1000)));
        accessToken.setTokenType(oAuthToken.getTokenType());
        return accessToken;
    }

}

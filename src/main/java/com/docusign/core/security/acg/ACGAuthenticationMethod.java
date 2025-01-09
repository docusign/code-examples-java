package com.docusign.core.security.acg;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import org.springframework.web.servlet.view.RedirectView;

import com.docusign.DSConfiguration;
import com.docusign.core.model.Session;
import com.docusign.core.security.SecurityHelpers;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;

public class ACGAuthenticationMethod {
    private static final String REDIRECT_URI = "/login/oauth2/code/acg";

    private static final String STATE = "random_state_string";

    private static String codeVerifier;

    private static String codeChallenge;

    public RedirectView initiateAuthorization(DSConfiguration configuration, List<String> scopes) throws Exception {
        codeVerifier = SecurityHelpers.generateCodeVerifier();
        codeChallenge = SecurityHelpers.generateCodeChallenge(codeVerifier);

        String authorizationURL = String.format(
                "%s&redirect_uri=%s&scope=%s&client_id=%s&state=%s&response_type=code&code_challenge=%s&code_challenge_method=S256",
                configuration.getAuthorizationEndpoint(),
                URLEncoder.encode(configuration.getAppUrl() + REDIRECT_URI, StandardCharsets.UTF_8),
                URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8), configuration.getUserId(), STATE,
                codeChallenge);

        return new RedirectView(authorizationURL);
    }

    public RedirectView exchangeCodeForToken(
            String oAuthToken,
            DSConfiguration configuration,
            Session session,
            String redirect,
            List<String> scopes) throws Exception {
        String requestBody = buildRequestBody(oAuthToken);
        String authHeader = generateAuthHeader(configuration);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(configuration.getTokenEndpoint()))
                .header("Authorization", "Basic " + authHeader)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            processTokenResponse(response.body(), configuration, session, scopes);
        } else {
            throw new IOException("Failed to exchange code for token. Status code: " + response.statusCode());
        }

        return new RedirectView(redirect);
    }

    private String buildRequestBody(String oAuthToken) throws IOException {
        return "grant_type=authorization_code" +
                "&code=" + URLEncoder.encode(oAuthToken, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                "&code_verifier=" + URLEncoder.encode(codeVerifier, StandardCharsets.UTF_8);
    }

    private String generateAuthHeader(DSConfiguration configuration) {
        return Base64.getEncoder().encodeToString(
                (configuration.getUserId() + ":" + configuration.getSecretUserId()).getBytes(StandardCharsets.UTF_8));
    }

    private void processTokenResponse(String responseBody, DSConfiguration configuration, Session session,
            List<String> scopes)
            throws Exception {
        ApiClient apiClient = new ApiClient(configuration.getBasePath());
        String accessToken = SecurityHelpers.parseJsonField(responseBody, "access_token");
        String expiresIn = SecurityHelpers.parseJsonField(responseBody, "expires_in");

        OAuth.UserInfo userInfo = apiClient.getUserInfo(accessToken);
        String accountId = userInfo.getAccounts().size() > 0 ? userInfo.getAccounts().get(0).getAccountId() : "";

        SecurityHelpers.setSpringSecurityAuthentication(scopes, accessToken, userInfo,
                accountId, session,
                expiresIn);
    }
}
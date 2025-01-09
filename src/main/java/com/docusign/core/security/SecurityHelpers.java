package com.docusign.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import com.docusign.core.model.Session;
import com.docusign.esign.client.auth.OAuth;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SecurityHelpers {
    public static String generateCodeVerifier() {
        byte[] randomBytes = new byte[32];
        new Random().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    public static String parseJsonField(String jsonResponse, String field) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);
        return jsonNode.get(field).asText();
    }

    public static void setSpringSecurityAuthentication(
            List<String> scopes,
            String oAuthToken,
            OAuth.UserInfo userInfo,
            String accountId,
            Session session,
            String expiresIn) {
        JWTOAuth2User principal = new JWTOAuth2User();
        principal.setAuthorities(scopes);
        principal.setCreated(userInfo.getCreated());
        principal.setName(userInfo.getName());
        principal.setGivenName(userInfo.getGivenName());
        principal.setFamilyName(userInfo.getFamilyName());
        principal.setSub(userInfo.getSub());
        principal.setEmail(userInfo.getEmail());
        principal.setAccounts(userInfo.getAccounts());
        principal.setAccessToken(new OAuth.OAuthToken().accessToken(oAuthToken));

        session.setTokenExpirationTime(System.currentTimeMillis() + Integer.parseInt(expiresIn) * 1000L);

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(principal, principal.getAuthorities(),
                accountId);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

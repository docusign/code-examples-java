package com.docusign;

import com.docusign.common.ApiIndex;
import com.docusign.core.model.ApiType;
import com.docusign.core.model.manifestModels.ManifestStructure;
import com.docusign.esign.client.auth.OAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Getter
@Setter
public class DSConfiguration {

    public Boolean isConsentRedirectActivated = false;

    public String apiTypeHeader = "ApiType";

    @Value("${com.docusign.github.example-uri}")
    private String exampleUrl;

    @Value("${com.docusign.documentation-path}")
    private String documentationPath;

    private String selectedApiType;

    private ManifestStructure codeExamplesText;

    private boolean additionalRedirect;

    private boolean shareAccessExampleScenario;

    private String principalUserId;

    @Value("${DS_TARGET_ACCOUNT_ID}")
    private String targetAccountId;

    @Value("${DS_APP_URL}")
    private String appUrl;

    @Value("${DS_BASE_PATH}")
    private String basePath;

    @Value("${DS_ROOMS_BASE_PATH}")
    private String roomsBasePath;

    @Value("${DS_CLICK_BASE_PATH}")
    private String clickBasePath;

    @Value("${DS_SIGNER_EMAIL:{USER_EMAIL}}")
    private String signerEmail;

    @Value("${DS_SIGNER_NAME:{USER_NAME}}")
    private String signerName;

    @Value("${Gateway_Account_Id}")
    private String gatewayAccountId;

    @Value("${spring.security.oauth2.client.registration.jwt.client-id}")
    private String userId;

    @Value("${spring.security.oauth2.client.registration.acg.client-secret}")
    private String secretUserId;

    @Value("${spring.security.oauth2.client.provider.acg.token-uri}")
    private String tokenEndpoint;

    @Value("${spring.security.oauth2.client.provider.acg.authorization-uri}")
    private String authorizationEndpoint;

    @Value("${jwt.grant.sso.redirect-url}")
    private String jwtRedirectURL;

    @Value("${authorization.code.grant.sso.redirect-url}")
    private String acgRedirectURL;

    @Value("${spring.security.oauth2.client.registration.jwt.private-key-path}")
    private String privateKeyPath;

    @Value("${spring.security.oauth2.client.registration.jwt.impersonated-user-guid}")
    private String impersonatedUserId;

    @Value("${jwt.grant.client.base-url}")
    private String baseURL;

    @Value("${Gateway_Name}")
    private String gatewayName;

    @Value("${Gateway_Display_Name}")
    private String gatewayDisplayName;

    @Value("${quickstart:{quickstart}}")
    private String quickstart;

    @Value("${quickACG:{quickACG}}")
    private String quickACG;

    @Value("${DS_MONITOR_BASE_PATH}")
    private String monitorBasePath;

    @Value("${DS_ADMIN_BASE_PATH}")
    private String adminBasePath;

    @Value("${DS_WEBFORMS_BASE_PATH}")
    private String webFormsBasePath;

    @Value("${CodeExamplesManifest}")
    private String codeExamplesManifest;

    public String getDsReturnUrl() {
        return appUrl + "/ds-return";
    }

    public String getDsPingUrl() {
        return appUrl + "/";
    }

    public String getBaseUrl(ApiIndex apiIndex, OAuth.Account oauthAccount) {
        if (ApiIndex.ROOMS.equals(apiIndex)) {
            return roomsBasePath;
        } else if (ApiIndex.CLICK.equals(apiIndex)) {
            return clickBasePath;
        } else if (ApiIndex.MONITOR.equals(apiIndex)) {
            return monitorBasePath;
        } else if (ApiIndex.ADMIN.equals(apiIndex)) {
            return adminBasePath;
        }

        return oauthAccount.getBaseUri();
    }

    public ApiType getSelectedApiType() {
        if (selectedApiType == null) {
            return ApiType.ESIGNATURE;
        }

        return ApiType.valueOf(selectedApiType);
    }

    public ApiIndex getSelectedApiIndex() {
        if (selectedApiType == null) {
            return ApiIndex.ESIGNATURE;
        }

        return ApiIndex.valueOf(selectedApiType);
    }

    public ManifestStructure getCodeExamplesText() {
        if (codeExamplesText != null) {
            return codeExamplesText;
        }

        try {
            codeExamplesText = new ObjectMapper().readValue(loadFileData(codeExamplesManifest),
                    ManifestStructure.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return codeExamplesText;
    }

    public String loadFileData(String linkToManifestFile) throws Exception {
        URL fullRequestPath = new URL(linkToManifestFile);
        HttpURLConnection httpConnection = (HttpURLConnection) fullRequestPath.openConnection();
        httpConnection.setRequestMethod(HttpMethod.GET);

        httpConnection.setRequestProperty(
                HttpHeaders.CONTENT_TYPE,
                String.valueOf(MediaType.APPLICATION_JSON));

        int responseCode = httpConnection.getResponseCode();

        if (responseCode < HttpURLConnection.HTTP_OK || responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new Exception(httpConnection.getResponseMessage());
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            stringBuilder.append(temp);
        }
        bufferedReader.close();
        httpConnection.disconnect();

        return stringBuilder.toString();
    }
}
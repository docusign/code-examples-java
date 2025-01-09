package com.docusign;

import com.docusign.core.model.manifestModels.ManifestStructure;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Getter
@Setter
public class DSConfiguration {

    @Value("${com.docusign.github.example-uri}")
    private String exampleUrl;

    @Value("${com.docusign.documentation-path}")
    private String documentationPath;

    private String selectedApiType;

    @Value("${authorization.code.grant.sso.redirect-url}")
    private String acgRedirectURL;

    private ManifestStructure codeExamplesText;

    @Value("${DS_TARGET_ACCOUNT_ID}")
    private String targetAccountId;

    @Value("${DS_APP_URL}")
    private String appUrl;

    @Value("${DS_BASE_PATH}")
    private String basePath;

    @Value("${jwt.grant.client.base-url}")
    private String baseURL;

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

    @Value("${Gateway_Name}")
    private String gatewayName;

    @Value("${Gateway_Display_Name}")
    private String gatewayDisplayName;

    @Value("${quickstart:{quickstart}}")
    private String quickstart;

    private String quickACG = "true";

    @Value("${DS_MONITOR_BASE_PATH}")
    private String monitorBasePath;

    @Value("${DS_ADMIN_BASE_PATH}")
    private String adminBasePath;

    @Value("${spring.security.oauth2.client.registration.acg.client-secret}")
    private String secretUserId;

    @Value("${spring.security.oauth2.client.provider.acg.token-uri}")
    private String tokenEndpoint;

    @Value("${spring.security.oauth2.client.provider.acg.authorization-uri}")
    private String authorizationEndpoint;

    @Value("${spring.security.oauth2.client.registration.jwt.client-id}")
    private String userId;

    public String examplesApiPath = "examplesApi.json";

    public String apiTypeHeader = "ApiType";

    @Value("${CodeExamplesManifest}")
    private String codeExamplesManifest;

    public String getDsReturnUrl() {
        return appUrl + "/ds-return";
    }

    public String getDsPingUrl() {
        return appUrl + "/";
    }

    public ManifestStructure getCodeExamplesText() {
        if (codeExamplesText != null) {
            return codeExamplesText;
        }

        try {
            String json = loadFileData(codeExamplesManifest);
            codeExamplesText = new ObjectMapper().readValue(json, ManifestStructure.class);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return codeExamplesText;
    }

    private String loadFileData(String filePath) throws Exception {
        URL fullRequestPath = new URL(filePath);
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

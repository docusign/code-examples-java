package com.docusign;

import com.docusign.common.ApiIndex;
import com.docusign.core.model.ApiType;
import com.docusign.core.model.manifestModels.ManifestStructure;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
@Getter
@Setter
public class DSConfiguration {

    @Value("${com.docusign.github.example-uri}")
    private String exampleUrl;

    @Value("${com.docusign.documentation-path}")
    private String documentationPath;

    private String selectedApiType;

    private ManifestStructure codeExamplesText;

    public Boolean isConsentRedirectActivated = false;

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

    @Value("${AdminManifest}")
    private String adminManifest;

    @Value("${ESignatureManifest}")
    private String eSignatureManifest;

    @Value("${ClickManifest}")
    private String clickManifest;

    @Value("${RoomsManifest}")
    private String roomsManifest;

    @Value("${MonitorManifest}")
    private String monitorManifest;

    public String configFilePath = "src/main/resources/application.json";

    public String examplesApiPath = "examplesApi.json";

    public String apiTypeHeader = "ApiType";

    public String getDsReturnUrl() {
        return appUrl + "/ds-return";
    }

    public String getDsPingUrl() {
        return appUrl + "/";
    }

    public ApiType getSelectedApiType() throws IOException {
        return ApiType.valueOf(getSelectedApi());
    }

    public ApiIndex getSelectedApiIndex() throws IOException {
        return ApiIndex.valueOf(getSelectedApi());
    }

    private String getSelectedApi() throws IOException {
        if (selectedApiType != null){
            return selectedApiType;
        }

        if(Boolean.valueOf(quickACG)){
            return ApiIndex.ESIGNATURE.name();
        }

        ClassPathResource resource = new ClassPathResource(examplesApiPath);
        String source = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        try {
            Object apiTypeValues = new JSONObject(source).get(apiTypeHeader);
            if (apiTypeValues == null ||  !EnumUtils.isValidEnum(ApiIndex.class, apiTypeValues.toString())) {
                throw new JSONException(String.format("The wrong format of the %s file.", examplesApiPath));
            }
            selectedApiType = apiTypeValues.toString();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return selectedApiType;
    }

    public ManifestStructure getCodeExamplesText() {
        if (codeExamplesText != null){
            return codeExamplesText;
        }

        try {
            String json = loadFileData(getTextManifestDependingOnCurrentAPI());
            codeExamplesText = new ObjectMapper().readValue(json, ManifestStructure.class);
        } catch (JSONException | IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return codeExamplesText;
    }

    private String getTextManifestDependingOnCurrentAPI() throws IOException {
        String linkToManifest = "";

        ApiIndex selectedApiIndex = getSelectedApiIndex();
        if (selectedApiIndex == ApiIndex.ESIGNATURE)
        {
            linkToManifest = eSignatureManifest;
        }
        else if (selectedApiIndex == ApiIndex.CLICK)
        {
            linkToManifest = clickManifest;
        }
        else if (selectedApiIndex == ApiIndex.ROOMS)
        {
            linkToManifest = roomsManifest;
        }
        else if (selectedApiIndex == ApiIndex.MONITOR)
        {
            linkToManifest = monitorManifest;
        }
        else if (selectedApiIndex == ApiIndex.ADMIN)
        {
            linkToManifest = adminManifest;
        }

        return linkToManifest;
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
package com.docusign;

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

    public String examplesApiPath = "examplesApi.json";

    public String apiTypeHeader = "ApiType";

    @Value("${ESignatureManifest}")
    private String eSignatureManifest;

    public String getDsReturnUrl() {
        return appUrl + "/ds-return";
    }

    public String getDsPingUrl() {
        return appUrl + "/";
    }

    public ManifestStructure getCodeExamplesText() {
        if (codeExamplesText != null){
            return codeExamplesText;
        }

        try {
            String json = loadFileData(eSignatureManifest);
            codeExamplesText = new ObjectMapper().readValue(json, ManifestStructure.class);
        } catch (JSONException | IOException e){
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
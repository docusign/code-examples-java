package com.docusign;


import com.docusign.common.ApiIndex;
import com.docusign.common.SelectedApiTypes;
import com.docusign.core.model.ApiType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import java.io.IOException;
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

    @Value("${DS_TARGET_ACCOUNT_ID}")
    private String targetAccountId;

    @Value("${DS_APP_URL}")
    private String appUrl;


    @Autowired
    private SelectedApiTypes apiName;

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

    @Value("${DS_MONITOR_BASE_PATH}")
    private String monitorBasePath;

    @Value("${DS_ADMIN_BASE_PATH}")
    private String adminBasePath;

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
}

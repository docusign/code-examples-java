package com.docusign.core.security.jwt;

import com.docusign.DSConfiguration;
import com.docusign.core.exception.LauncherException;
import com.docusign.core.model.ApiType;
import com.docusign.esign.client.ApiClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
//import org.springframework.security.oauth2.client.token.grant.redirect.AbstractRedirectResourceDetails;
import org.springframework.util.FileCopyUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class JWTAuthorizationCodeResourceDetails /*extends AbstractRedirectResourceDetails*/ {

    private String privateKeyPath;
    private String impersonatedUserGuid;
    private String baseUrl;

    @Autowired
    private DSConfiguration dsConfiguration;

    private FileSystemResource rsaPrivateKey;
    private final ApiClient apiClient = new ApiClient();

    public byte[] getRsaBytes() throws LauncherException {
        if (rsaPrivateKey == null) {
            rsaPrivateKey = new FileSystemResource(privateKeyPath);
        }
        try {
            return FileCopyUtils.copyToByteArray(rsaPrivateKey.getInputStream());
        } catch (IOException e) {
            throw new LauncherException("Error reading rsa file. Check configuration");
        }
    }

    public ApiClient getApiClient() {
        return apiClient;
    }


    public List<String> getScopes() throws IOException {
        ApiType apiType = dsConfiguration.getSelectedApiType();

        return Arrays.asList(apiType.getScopes());

    }
}

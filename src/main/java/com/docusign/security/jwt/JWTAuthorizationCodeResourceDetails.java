package com.docusign.security.jwt;

import com.docusign.esign.client.ApiClient;
import com.docusign.exception.LauncherException;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.client.token.grant.redirect.AbstractRedirectResourceDetails;
import org.springframework.util.FileCopyUtils;

@Getter
@Setter
public class JWTAuthorizationCodeResourceDetails extends AbstractRedirectResourceDetails {

    private String privateKeyPath;
    private String impersonatedUserGuid;
    private String baseUrl;

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

}

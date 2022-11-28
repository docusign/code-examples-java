package tests.common;

import com.docusign.esign.client.ApiClient;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@Setter
public class TestConfig {
    private String ClientId;

    private String Host;

    private String AccountId;

    private String SignerEmail;

    private String SignerName;

    private String ImpersonatedUserId;

    private String OAuthBasePath;

    private String PrivateKey;

    private String AccessToken;

    private String BasePath;

    private String TemplateId;

    public String PathToDocuments;

    private static TestConfig single_instance = null;

    public static TestConfig getInstance() throws IOException {
        if (single_instance == null)
            single_instance = new TestConfig();

        return single_instance;
    }

    public TestConfig() throws IOException {
        PathToDocuments = System.getProperty("user.dir") + "//src//main//resources//";
        Host = "https://demo.docusign.net/restapi";
        OAuthBasePath = "account-d.docusign.com";
        PrivateKey = PathToDocuments + "private.key";

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("application.json").getFile());
        String source = new String(Files.readAllBytes(Paths.get(file.getPath())));

        try {
            Object impersonatedUserId = new JSONObject(source).get("jwt.grant.client.impersonated-user-guid");
            Object clientId = new JSONObject(source).get("jwt.grant.client.client-id");
            Object signerEmail = new JSONObject(source).get("DS_SIGNER_EMAIL");
            Object signerName = new JSONObject(source).get("DS_SIGNER_NAME");
            Object basePath = new JSONObject(source).get("DS_BASE_PATH");

            if (impersonatedUserId == null || clientId == null || signerEmail == null || signerName == null) {
                throw new JSONException("The wrong format of the application.json file.");
            }

            ImpersonatedUserId = impersonatedUserId.toString();
            ClientId = clientId.toString();
            SignerEmail = signerEmail.toString();
            SignerName = signerName.toString();
            BasePath = basePath.toString();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}

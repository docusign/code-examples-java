package tests.common;

import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.esign.model.Brand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
public class TestConfig {
    private static TestConfig single_instance = null;

    public String PathToDocuments;

    private String ClientId;

    private String Host;

    private String AccountId;

    private String SignerEmail;

    private String SignerName;

    private String ImpersonatedUserId;

    private String OAuthBasePath;

    private String PrivateKeyPath;

    private String PrivateKey;

    private String AccessToken;

    private String BasePath;

    private String BatchId;

    private String TemplateId;

    private ClickwrapVersionSummaryResponse inactiveClickwrap;

    private Brand brand;

    public TestConfig() throws IOException {
        PathToDocuments = System.getProperty("user.dir") + "//src//main//resources//";
        Host = "https://demo.docusign.net";
        OAuthBasePath = "account-d.docusign.com";
        PrivateKeyPath = PathToDocuments + "private.key";
        BasePath = "https://demo.docusign.net";

        try {
            var isFilePresentInTheRepository = Files.exists(Path.of(PathToDocuments + "application.json"));

            if (isFilePresentInTheRepository) {
                String source = new String(Files.readAllBytes(Paths.get(PathToDocuments + "application.json")));

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                JsonObject originalJsonValue = gson.fromJson(source, JsonObject.class);
                JsonObject jwtConfigurationValues = originalJsonValue
                        .getAsJsonObject("spring")
                        .getAsJsonObject("security")
                        .getAsJsonObject("oauth2")
                        .getAsJsonObject("client")
                        .getAsJsonObject("registration")
                        .getAsJsonObject("jwt")
                        .getAsJsonObject();

                Object impersonatedUserId = jwtConfigurationValues.get("impersonated-user-guid").toString();
                Object clientId = jwtConfigurationValues.get("client-id").toString();
                Object signerEmail = new JSONObject(source).get("DS_SIGNER_EMAIL");
                Object signerName = new JSONObject(source).get("DS_SIGNER_NAME");

                if (impersonatedUserId == null || clientId == null || signerEmail == null || signerName == null) {
                    throw new JSONException("The wrong format of the application.json file.");
                }

                ImpersonatedUserId = impersonatedUserId.toString().replace("\"", "");
                ClientId = clientId.toString().replace("\"", "");
                SignerEmail = signerEmail.toString();
                SignerName = signerName.toString();
            } else {
                ImpersonatedUserId = System.getenv("IMPERSONATED_USER_ID");
                ClientId = System.getenv("CLIENT_ID");
                SignerEmail = System.getenv("SIGNER_EMAIL");
                SignerName = System.getenv("SIGNER_NAME");
                PrivateKey = System.getenv("PRIVATE_KEY");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static TestConfig getInstance() throws IOException {
        if (single_instance == null) {
            single_instance = new TestConfig();
        }

        return single_instance;
    }
}

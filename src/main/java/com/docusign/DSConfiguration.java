package com.docusign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DSConfiguration {

    @Value("${DS_APP_URL:{APP_URL}}")
    public String appUrl;

    @Value("${DS_SIGNER_EMAIL:{USER_EMAIL}}")
    private String signerEmail;

    public String getSignerEmail() {
        return signerEmail;
    }

    public void setSignerEmail(String value) {
        signerEmail = value;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    @Value("${DS_SIGNER_NAME:{USER_NAME}}")
    private String signerName;
    @Value("${Gateway_Account_Id}")
    public String gatewayAccountId;
    @Value("${Gateway_Name}")
    public String gatewayName;
    @Value("${Gateway_Display_Name}")
    public String gatewayDisplayName;

    public boolean production = false;
    public boolean debug = true; // Send debugging statements to console
    public String sessionSecret = "12345"; // Secret for encrypting session cookie content
    public boolean allowSilentAuthentication = true; // a user can be silently authenticated if they have an
    // active login session on another tab of the same browser
    // Set if you want a specific DocuSign AccountId, If null, the user's default account will be used.
    public String targetAccountId = null;
    public String demoDocPath = "demo_documents";
    public String docDocx = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    public String docPdf = "World_Wide_Corp_lorem.pdf";
    public String githubExampleUrl = "https://github.com/docusign/eg-03-java-auth-code-grant/blob/master/src/main/java/";
    public String documentation = null;
}

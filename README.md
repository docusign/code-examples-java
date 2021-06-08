# Java Launcher Code Examples

### GitHub repo: [code-examples-java](./README.md)

This GitHub repo includes code examples for the DocuSign eSignature REST API, Rooms API, Click API, and Monitor API. To switch between API code examples, in the application.json file, change the `DS_API_NAME` setting from `ESIGNATURE` to `ROOMS`, `CLICK`, or `MONITOR`.  


## Introduction

This repo is a Java Spring Boot application that supports the following authentication workflows:

* Authentication with DocuSign via [Authorization Code Grant](https://developers.docusign.com/platform/auth/authcode).
When the token expires, the user is asked to re-authenticate. The refresh token is not used.

* Authentication with DocuSign via [JSON Web Token (JWT) Grant](https://developers.docusign.com/platform/auth/jwt/).
When the token expires, it updates automatically.

The [Spring Boot security Oauth2 boot autoconfigure package](https://github.com/spring-projects/spring-security-oauth2-boot/blob/master/spring-security-oauth2-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/security/oauth2/resource/UserInfoRestTemplateCustomizer.java) is used for authentication.

The OAuth library is used in the file [WebSecurityConfig.java](https://github.com/docusign/code-examples-java/blob/master/src/main/java/com/docusign/WebSecurityConfig.java).


## eSignature API

For more information about the scopes used for obtaining authorization to use the eSignature API, see [Required scopes](https://developers.docusign.com/docs/esign-rest-api/esign101/auth#required-scopes).


For a list of code examples that use the eSignature API, select the Java tab under [Examples and languages](https://developers.docusign.com/docs/esign-rest-api/how-to/code-launchers#examples-and-languages) on the DocuSign Developer Center.


## Rooms API 

**Note:** To use the Rooms API, you must also [create your Rooms developer account](https://developers.docusign.com/docs/rooms-api/rooms101/create-account). Examples 4 and 6 require that you have the DocuSign Forms feature enabled in your Rooms for Real Estate account.  
For more information about the scopes used for obtaining authorization to use the Rooms API, see [Required scopes](https://developers.docusign.com/docs/rooms-api/rooms101/auth/).

For a list of code examples that use the Rooms API, select the Java tab under [Examples and languages](https://developers.docusign.com/docs/rooms-api/how-to/code-launchers#examples-and-languages) on the DocuSign Developer Center.


## Click API  
For more information about the scopes used for obtaining authorization to use the Click API, see [Required scopes](https://developers.docusign.com/docs/click-api/click101/auth/#required-scopes)  


For a list of code examples that use the Click API, select the Java tab under [Examples and languages](https://developers.docusign.com/docs/click-api/how-to/code-launchers#examples-and-languages) on the DocuSign Developer Center.


## Monitor API

**Note:** To use the Monitor API, you must also [enable DocuSign Monitor for your organization](https://developers.docusign.com/docs/monitor-api/how-to/enable-monitor/).  
For information about the scopes used for obtaining authorization to use the Monitor API, see the [scopes section](https://developers.docusign.com/docs/monitor-api/monitor101/auth/).

For a list of code examples that use the Click API, select the Java tab under [Examples and languages](https://developers.docusign.com/docs/monitor-api/how-to/code-launchers#examples-and-languages) on the DocuSign Developer Center.

## Installation

### Prerequisites
**Note:** If you downloaded this code using [Quickstart](https://developers.docusign.com/docs/esign-rest-api/quickstart/) from the DocuSign Developer Center, skip items 1 and 2 as they were automatically performed for you.

1. A free [DocuSign developer account](https://go.docusign.com/o/sandbox/); create one if you don't already have one.
1. A DocuSign app and integration key that is configured to use either [Authorization Code Grant](https://developers.docusign.com/platform/auth/authcode/) or [JWT Grant](https://developers.docusign.com/platform/auth/jwt/) authentication.

   This [video](https://www.youtube.com/watch?v=eiRI4fe5HgM) demonstrates how to obtain an integration key.  
   
   To use [Authorization Code Grant](https://developers.docusign.com/platform/auth/authcode/), you will need an integration key and a secret key. See [Installation steps](#installation-steps) for details.  

   To use [JWT Grant](https://developers.docusign.com/platform/auth/jwt/), you will need an integration key, an RSA key pair, and the API Username GUID of the impersonated user. See [Installation steps for JWT Grant authentication](#installation-steps-for-jwt-grant-authentication) for details.  

   For both authentication flows:  
   
   If you use this launcher on your own workstation, the integration key must include redirect URIs of     

   * http://localhost:8080/login&type=acg
   * http://localhost:8080/login&type=jwt

   If you host this launcher on a remote web server, set your redirect URI as   
   
   {base_url}/login
   
   where {base_url} is the URL for the web app.  

1. [JDK 11](https://jdk.java.net/java-se-ri/11) or later
1. [Maven](https://maven.apache.org/download.cgi)


### Installation steps

**Note:** If you downloaded this code using [Quickstart](https://developers.docusign.com/docs/esign-rest-api/quickstart/) from the DocuSign Developer Center, skip step 4 as it was automatically performed for you.

1. Extract the Quickstart ZIP file or download or clone the code-examples-java repository.
1. In your command-line environment, switch to the folder:  
   `cd <Quickstart folder>` or `cd code-examples-java`
1. Package the code: `mvn package`
1. To configure the launcher for [Authorization Code Grant](https://developers.docusign.com/platform/auth/authcode/) authentication, create a copy of the file src/main/resources/application.example.json and save the copy as src/main/resources/application.json.
   1. Add your API Username. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **My Account Information**, copy the **API Username** GUID and save it in application.json as your `DS_TARGET_ACCOUNT_ID`.
   1. Add your integration key. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **Apps and Integration Keys**, choose the app to use, then select **Actions > Edit**. Under **General Info**, copy the **Integration Key** GUID and save it in application.json as your `authorization.code.grant.client.client-id`.
   1. Generate a secret key, if you don’t already have one. Under **Authentication**, select **+ ADD SECRET KEY**. Copy the secret key and save it in application.json as your `authorization.code.grant.client.client-secret`.
   1. Add the launcher’s redirect URI. Under **Additional settings**, select **+ ADD URI**, and set a redirect URI of http://localhost:8080/login&type=acg. Select **SAVE**.   
   1. Set a name and email address for the signer. In application.json, save an email address as `DS_SIGNER_EMAIL` and a name as `DS_SIGNER_NAME`.  
**Note:** Protect your personal information. Please make sure that application.json will not be stored in your source code repository.  
1. Run the launcher: `java -Dspring.profiles.active=dev -jar target/code-examples-java-1.0-SNAPSHOT.war`

  
### Installation steps for JWT Grant authentication

**Note:** If you downloaded this code using [Quickstart](https://developers.docusign.com/docs/esign-rest-api/quickstart/) from the DocuSign Developer Center, skip step 4 as it was automatically performed for you.  
Also, in order to select JSON Web Token authentication in the launcher, in src/main/resources/application.json, change the `quickstart` setting to `false`.

1. Extract the Quickstart ZIP file or download or clone the code-examples-java repository.
1. In your command-line environment, switch to the folder: `cd <Quickstart folder>` or `cd code-examples-java`
1. Package the code: `mvn package`
1. To configure the launcher for [JWT Grant](https://developers.docusign.com/platform/auth/jwt/) authentication, create a copy of the file src/main/resources/application.example.json and save the copy as src/main/resources/application.json.
   1. Add your API Username. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **My Account Information**, copy the **API Username** GUID and save it in application.json as your `DS_TARGET_ACCOUNT_ID` and as your `jwt.grant.client.impersonated-user-guid`.
   1. Add your integration key. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **Apps and Integration Keys**, choose the app to use, then select **Actions > Edit**. Under **General Info**, copy the **Integration Key** GUID and save it in application.json as your `jwt.grant.client.client-id`.
   1. Generate an RSA key pair, if you don’t already have one. Under **Authentication**, select **+ GENERATE RSA**. Copy the private key, and save it in a new file named src/main/resources/private.key.   
   1. Add the launcher’s redirect URI. Under **Additional settings**, select **+ ADD URI**, and set a redirect URI of http://localhost:8080/login&type=jwt. Select **SAVE**.   
   1. Set a name and email address for the signer. In application.json, save an email address as `DS_SIGNER_EMAIL` and a name as `DS_SIGNER_NAME`.  
**Note:** Protect your personal information. Please make sure that application.json will not be stored in your source code repository.  
1. Run the launcher: `java -Dspring.profiles.active=dev -jar target/code-examples-java-1.0-SNAPSHOT.war`  
   The launcher automatically opens.  
1. On the black navigation bar, select **Login**.
1. From the picklist, select **JSON Web Token (JWT) grant** > **Authenticate with DocuSign**.
1. When prompted, log in to your DocuSign developer account. If this is your first time using the app, select **ACCEPT** at the consent window. 
3. Select your desired code example.


## IntelliJ Ultimate instructions for Windows  

IntelliJ IDEA can be used with the launcher. The [IntelliJ IDEA Ultimate edition](https://www.jetbrains.com/idea/download/#section=windows) is required due to its support for Spring Boot and JSP view pages.

**Note:** If you downloaded this code using [Quickstart](https://developers.docusign.com/docs/esign-rest-api/quickstart/) from the DocuSign Developer Center, skip step 2 as it was automatically performed for you.

1. Extract the Quickstart ZIP file or download or clone the code-examples-java repository. 
1. To configure the launcher for [Authorization Code Grant](https://developers.docusign.com/platform/auth/authcode/) authentication, create a copy of the file src/main/resources/application.example.json and save the copy as src/main/resources/application.json.
   1. Add your API Username. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **My Account Information**, copy the **API Username** GUID and save it in application.json as your `DS_TARGET_ACCOUNT_ID`.
   1. Add your integration key. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **Apps and Integration Keys**, choose the app to use, then select **Actions > Edit**. Under **General Info**, copy the **Integration Key** GUID and save it in application.json as your `authorization.code.grant.client.client-id`.
   1. Generate a secret key, if you don’t already have one. Under **Authentication**, select **+ ADD SECRET KEY**. Copy the secret key and save it in application.json as your `authorization.code.grant.client.client-secret`.
   1. Add the launcher’s redirect URI. Under **Additional settings**, select **+ ADD URI**, and set a redirect URI of http://localhost:8080/login&type=acg. Select **SAVE**.   
   1. Set a name and email address for the signer. In application.json, save an email address as `DS_SIGNER_EMAIL` and a name as `DS_SIGNER_NAME`.  
**Note:** Protect your personal information. Please make sure that application.json will not be stored in your source code repository.  
1. Start IntelliJ IDEA Ultimate. In the **Welcome to IntelliJ IDEA** dialog box, select **Open**.  
   1. In the **Open File or Project** dialog box, select your Quickstart folder or code-examples-java, then select **OK**.
   1. In **Open or Import Project** dialog box, select **Maven project**, then select **OK**. 
1. Create a Java application configuration: In the top navigation bar, select **Run** > **Edit configurations**.  
   1. In the far left corner, select **+** > **Spring Boot**.
   1. To the right of **Main class**, select **... > App (com.docusign) code-examples-java**, then select **OK**.
   1. Under **Spring Boot**, select the **Enable debug output** checkbox, then select **OK**.
1. Run the launcher: In the top navigation bar, select **Run > Run 'App'**. The launcher automatically opens.   
   When prompted, log in to your DocuSign developer account. If this is your first time using the app, select **ACCEPT** at the consent window. 
1. [Optional] To install Lombok, select **File > Settings > Plugins > Marketplace** tab. To the right of the magnifying glass icon, input: `Lombok`  
   It should have an author named Michail Plushnikov. Select **Install**, then restart IntelliJ.
   

## Eclipse instructions for Windows    

**Note:** If you downloaded this code using [Quickstart](https://developers.docusign.com/docs/esign-rest-api/quickstart/) from the DocuSign Developer Center, skip step 2 as it was automatically performed for you.

1. Extract the Quickstart ZIP file or download or clone the code-examples-java repository.  
1. To configure the launcher for [Authorization Code Grant](https://developers.docusign.com/platform/auth/authcode/) authentication, create a copy of the file src/main/resources/application.example.json and save the copy as src/main/resources/application.json.
   1. Add your API Username. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **My Account Information**, copy the **API Username** GUID and save it in application.json as your `DS_TARGET_ACCOUNT_ID`.
   1. Add your integration key. On the [Apps and Keys](https://admindemo.docusign.com/authenticate?goTo=apiIntegratorKey) page, under **Apps and Integration Keys**, choose the app to use, then select **Actions > Edit**. Under **General Info**, copy the **Integration Key** GUID and save it in application.json as your `authorization.code.grant.client.client-id`.
   1. Generate a secret key, if you don’t already have one. Under **Authentication**, select **+ ADD SECRET KEY**. Copy the secret key and save it in application.json as your `authorization.code.grant.client.client-secret`.
   1. Add the launcher’s redirect URI. Under **Additional settings**, select **+ ADD URI**, and set a redirect URI of http://localhost:8080/login&type=acg. Select **SAVE**.   
   1. Set a name and email address for the signer. In application.json, save an email address as `DS_SIGNER_EMAIL` and a name as `DS_SIGNER_NAME`.  
**Note:** Protect your personal information. Please make sure that application.json will not be stored in your source code repository.  

1. Open Eclipse and select **File > Import**. In the **Import** dialog box, select **Existing Maven Project > Next > Browse**, browse for your Quickstart folder or code-examples-java, then **Select Folder**. 
   1. Under **Projects**, the `pom.xml` file should be selected. 
   1. To save the project link to your Eclipse workspace, select the **Add project(s) to working set** checkbox. Select **Finish**.
1. Next, select **Run**, then **Run Configurations**, and right-click **Maven Build**, then **New Configuration** to clean and compile the package.
   1. In the **Name** field, input: `build`
   1. In **Base directory**, input: `${workspace_loc:/code-examples-java}`
   1. In **Goals**, input: `clean package`
   1. Select the **JRE** tab. Under **Runtime JRE**, ensure it lists jdk-11 or higher. Select **Apply** to save.
1. In the **Run Configurations** dialog box, in the left sidebar, right-click **Java Application**, then select **New Configuration**.  
   1. In the **Name** field, input: `App`
   1. In the **Project** field, input: `code-examples-java`
   1. In the **Main class** field, input: `com.docusign.App`
   1. Select the **JRE** tab. Under **Runtime JRE**, ensure it lists jdk-11 or higher. Select **Apply** to save.
   1. Select **Run** to run the launcher.  The launcher automatically opens.   
   When prompted, log in to your DocuSign developer account. If this is your first time using the app, select **ACCEPT** at the consent window. 
1. [Optional] Download [lombok.jar](https://projectlombok.org/downloads/lombok.jar) to your local machine, then run the following command to install Lombok: `java -jar lombok.jar`  


## Payments code example  

To use the payments code example, create a test payment gateway on the [**Payments**](https://admindemo.docusign.com/authenticate?goTo=payments) page in your developer account. See [Configure a payment gateway](./PAYMENTS_INSTALLATION.md) for details.

Once you've created a payment gateway, save the **Gateway Account ID** GUID to application.json.


## License and additional information  

### License  
This repository uses the MIT License. See [LICENSE](./LICENSE) for details.

### Pull Requests
Pull requests are welcomed. Pull requests will only be considered if their content
uses the MIT License.

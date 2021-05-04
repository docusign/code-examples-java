# Java Launcher Code Examples

Github repo: https://github.com/docusign/code-examples-java

This GitHub repo includes code example for the DocuSign eSignature REST API, for the DocuSign Rooms API and for the Click API. 

To use the Rooms API code examples, modify the **DS_API_NAME** setting at the end of the application.json file. Set the value to `ROOMS`.
To use the Click API code examples, modify the **DS_API_NAME** setting at the end of the application.json file. Set the value to `CLICK`.

**Note:** to use the Rooms API you must also [create your DocuSign Developer Account for Rooms](https://developers.docusign.com/docs/rooms-api/rooms101/create-account).


## Introduction
This repo is a Java Spring Boot application that demonstrates how to authenticate with DocuSign via the
[Authorization Code Grant flow](https://developers.docusign.com/esign-rest-api/guides/authentication/oauth2-code-grant). When the token expires, the user is asked to reauthenticate. The refresh token is not used.

The [Spring Boot security Oauth2 boot autoconfigure package](https://github.com/spring-projects/spring-security-oauth2-boot/blob/master/spring-security-oauth2-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/security/oauth2/resource/UserInfoRestTemplateCustomizer.java) is used for authentication.

The OAuth library is used in the file [WebSecurityConfig.java](https://github.com/docusign/code-examples-java/blob/master/src/main/java/com/docusign/WebSecurityConfig.java).


## eSignature API

For more information about the scopes used for obtaining authorization to use the eSignature API, see the [Required Scopes section](https://developers.docusign.com/docs/esign-rest-api/esign101/auth).

1. **Use embedded signing.**  
   [Source](./src/main/java/com/docusign/EG001ControllerEmbeddedSigning.java)
   This example sends an envelope, and then uses embedded signing for the first signer. With embedded signing, DocuSign signing is initiated from your website.
1. **Request a signature by email (Remote Signing).**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG002ControllerSigningViaEmail.java) The envelope includes a pdf, Word, and HTML document.
   Anchor text ([AutoPlace](https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience)) is used to position the signing fields in the documents.
1. **List envelopes in the user's account.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG003ControllerListEnvelopes.java)
1. **Get an envelope's basic information.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG004ControllerEnvelopeInfo.java)
   The example lists the basic information about an envelope, including its overall status.
1. **List an envelope's recipients and their current status.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG005ControllerEnvelopeRecipients.java)
1. **List an envelope's documents.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG006ControllerEnvelopeDocs.java)
1. **Download an envelope's documents.**    
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG007ControllerEnvelopeGetDoc.java) This example can download individual documents, the documents concatenated together, or a zip file of the documents.  
1. **Programmatically create a template.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG008ControllerCreateTemplate.java)  
1. **Request a signature by email using a template.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG009ControllerUseTemplate.java)  
1. **Send an envelope and upload its documents with multipart binary transfer.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG010ControllerSendBinaryDocs.java) Binary transfer is 33% more efficient than using Base64 encoding.  
1. **Use embedded sending.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG011ControllerEmbeddedSending.java)  
1. **Embedded DocuSign web tool (NDSE).**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG012ControllerEmbeddedConsole.java)  
1. **Use embedded signing from a template with an added document.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG013ControllerAddDocToTemplate.java)
   This example sends an envelope based on a template. In addition to the template's document(s), the example adds an additional document to the envelope by using the [Composite Templates](https://developers.docusign.com/esign-rest-api/guides/features/templates#composite-templates) feature.  
1. **Payments example: an order form, with online payment by credit card.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG014ControllerCollectPayment.java)  
1. **Get the envelope tab data.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG015ControllerGetTabValues.java) Retrieve the tab (field) values for all of the envelope's recipients.  
1. **Set envelope tab values.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG016ControllerSetTabValues.java) The example creates an envelope and sets the initial values for its tabs (fields). Some of the tabs are set to be read-only, others can be updated by the recipient. The example also stores metadata with the envelope.  
1. **Set template tab values.**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG017ControllerSetTemplateTabValues.java) The example creates an envelope using a template and sets the initial values or its tabs (fields). The example also stores metadata with the envelope.  
1. **Get the envelope custom field data (metadata).**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG018ControllerEnvelopeCustomFieldValues.java) The example retrieves the custom metadata (custom data fields) stored with the envelope.  
1. **Requiring an Access Code for a Recipient**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG019ControllerAccessCodeAuthentication.java)
   This example sends and envelope that requires an access-code for the purpose of multi-factor authentication.  
1. **Requiring SMS authentication for a recipient**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG020ControllerSmsAuthentication.java) This example sends and envelope that requires entering in a six digit code from an text message for the purpose of multi-factor authentication.  
1. **Requiring Phone authentication for a recipient**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG021ControllerPhoneAuthentication.java) This example sends and envelope that requires entering in a voice-based response code for the purpose of multi-factor authentication.  
1. **Requiring Knowledge-Based Authentication (KBA) for a Recipient**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG022ControllerKBAAuthentication.java) This example sends and envelope that requires passing a Public records check to validate identity for the purpose of multi-factor authentication.  
1. **Requiring ID Verification (IDV) for a recipient**  
    [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG023ControllerIdvAuthentication.java) This example sends and envelope that requires the recipient to upload a government issued id.  
1. **Creating a permission profile**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG024ControllerPermissionCreate.java) This code example demonstrates how to create a user group's permission profile using the [Create Profile](https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/create) method.  
1. **Setting a permission profile**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG025ControllerPermissionSetUserGroups.java) This code example demonstrates how to set a user group's permission profile using the [Update Group](https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/update) method. You must have already created permissions profile and group of users.  
1. **Updating individual permission settings**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG026ControllerPermissionChangeSingleSetting.java) This code example demonstrates how to update individual settings for a specific permission profile using the [Update Permission Profile](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/update) method. You must have already created permissions profile and group of users.  
1. **Deleting a permission profile**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG027ControllerPermissionDelete.java)
   This code example demonstrates how to an account's permission profile using the [Delete AccountPermissionProfiles](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/delete) method. 
1. **Creating a brand**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG028ControllerCreateBrand.java)
   This example creates brand profile for an account using the [Create Brand](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountBrands/create) method.
1. **Applying a brand to an envelope**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG029ControllerApplyBrandToEnvelope.java) This code example demonstrates how to apply a brand you've created to an envelope using the [Create Envelope](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. 
   First, creates the envelope and then applies brand to it.
   Anchor text ([AutoPlace](https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience)) is used to position the signing fields in the documents.
1. **Applying a brand to a template**
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG030ControllerApplyBrandToTemplate.java) This code example demonstrates how to apply a brand you've created to a template using using the [Create Envelope](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. You must have at least one created template and brand.  
1. **Bulk sending envelopes to multiple recipients**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG031ControllerBulkSendEnvelopes.java) This example creates and sends a bulk envelope by generating a bulk recipient list and initiating a bulk send.
1. **Pausing a signature workflow Source**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG032ControllerPauseSignatureWorkflow.java) This code example demonstrates how to create an envelope where the workflow is paused before the envelope is sent to a second recipient.
1. **Unpausing a signature workflow**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG033ControllerUnpauseSignatureWorkflow.java) This code example demonstrates how to resume an envelope workflow that has been paused
1. **Use conditional recipients**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG034ControllerUseConditionalRecipients.java) This code example demonstrates how to create an envelope where the workflow is paused before the envelope is sent to a second recipient.
1. **Request a signature by SMS delivery**  
   [Source](./src/main/java/com/docusign/controller/eSignature/examples/EG035ControllerSMSDelivery.java) This code example demonstrates how to send a signature request via an SMS message using the [Envelopes: create](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. 


## Rooms API 

For more information about the scopes used for obtaining authorization to use the Rooms API, see the [Required Scopes section](https://developers.docusign.com/docs/rooms-api/rooms101/auth/).

**Note:** To use the Rooms API you must also [create your DocuSign Developer Account for Rooms](https://developers.docusign.com/docs/rooms-api/rooms101/create-account). Examples 4 and 6 require that you have the DocuSign Forms feature enabled in your Rooms for Real Estate account.

1. **Create room with Data.**  
   [Source](./src/main/java/com/docusign/controller/rooms/examples/R001ControllerCreateRoom.java)
   This code example creates a new room in your DocuSign Rooms account to be used for a transaction.
1. **Create a room from a template.**  
   [Source](./src/main/java/com/docusign/controller/rooms/examples/R002ControllerCreateRoomWithTemplate.java)
   This code example creates a new room using a template.
1. **Create room with Data.**  
   [Source](./src/main/java/com/docusign/controller/rooms/examples/R003ControllerExportingDataFromRoom.java)
   This code example exports all the avialalble data from a specific room in your DocuSign Rooms account.
1. **Add forms to a room.**  
   [Source](./src/main/java/com/docusign/controller/rooms/examples/R004ControllerAddingFormsToRoom.java)
   This code example adds a standard real estate related form to a specific room in your DocuSign Rooms account.
1. **How to search for rooms with filters.**  
   [Source](./src/main/java/com/docusign/controller/rooms/examples/R005ControllerGetRoomsWithFilters.java)
   This code example searches for rooms in your DocuSign Rooms account using a specific filter. 
1. **Create an external form fillable session.**  
   [Source](./src/main/java/com/docusign/controller/rooms/examples/R006ControllerCreateExternalFormFillSession.java)
   This code example create an external form that can be filled using DocuSign for a specific room in your DocuSign Rooms account.
   This example create an external form that can be filled using DocuSign for a specific room in your DocuSign Rooms account.
1. **Create a form group.**
   [Source.](./src/main/java/com/docusign/controller/rooms/examples/R007ControllerCreateFormGroup.java)
   This code example create a form group for your DocuSign Rooms for Real Estate account.
1. **Grant office access to a form group.**
   [Source.](./src/main/java/com/docusign/controller/rooms/examples/R008ControllerGrantOfficeAccessToFormGroup.java)
   This code example assign an office to a form group for your DocuSign Rooms for Real Estate account. 
   Granting office access to a form group will enable you to filter which form groups are available based on that office.
1. **Assign a form to a form group.**
   [Source.](./src/main/java/com/docusign/controller/rooms/examples/R009ControllerAssignFormToFormGroup.java)
   This example demonstrates how to assign a form to a form group for your DocuSign Rooms for Real 
   Estate account. As a prerequisite, ensure that you have created a form group and set the 
   office ID on this form group before proceeding.


## Click API  
For more information about the scopes used for obtaining authorization to use the Click API, see the [Required Scopes section](https://developers.docusign.com/docs/click-api/click101/auth/)  
  
1. **Create Clickwraps.**  
   [Source](./src/main/java/com/docusign/controller/click/examples/C001ControllerCreateClickwrap.java) This code example shows how to create a clickwrap.
1. **Activate Clickwrap.**  
   [Source](./src/main/java/com/docusign/controller/click/examples/C002ControllerActivateClickwrap.java)
   This code example shows how to activate a new clickwrap that you have already created.
1. **Clickwrap Versioning.**  
   [Source](./src/main/java/com/docusign/controller/click/examples/C003ControllerCreateNewVersionClickwrap.java)
   This code example shows how to create a new clickwrap version.
1. **Get a list of Clickwraps.**  
   [Source](./src/main/java/com/docusign/controller/click/examples/C004ControllerGetListClickwraps.java)
   This code example shows how to get a list of clickwraps.
1. **Get Clickwrap Responses.**  
   [Source](./src/main/java/com/docusign/controller/click/examples/C005ControllerGetClickwrapResponses.java)
   This code example shows how to get clickwrap responses.


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
   

## Eclipse instructions

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


### Payments code example  
To use the payments code example, create a test payment gateway on the [**Payments**](https://admindemo.docusign.com/authenticate?goTo=payments) page in your developer account. See [Configure a payment gateway](./PAYMENTS_INSTALLATION.md) for details.

Once you've created a payment gateway, save the **Gateway Account ID** GUID to application.json.


## License and additional information  

### License  
This repository uses the MIT License. See [LICENSE](./LICENSE) for details.

### Pull Requests
Pull requests are welcomed. Pull requests will only be considered if their content
uses the MIT License.

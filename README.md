# Java Launcher Code Examples

### Github repo: code-examples-java
## Introduction
This repo is a Java Spring Boot application that demonstrates:

1. **Embedded Signing.**
   [Source.](./src/main/java/com/docusign/EG001ControllerEmbeddedSigning.java)
   This example sends an envelope, and then uses embedded signing for the first signer.
   With embedded signing, DocuSign signing is initiated from your website.
1. **Send an envelope with a remote (email) signer and cc recipient.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG002ControllerSigningViaEmail.java)
   The envelope includes a pdf, Word, and HTML document.
   Anchor text ([AutoPlace](https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience)) is used to position the signing fields in the documents.
1. **List envelopes in the user's account.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG003ControllerListEnvelopes.java)
1. **Get an envelope's basic information.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG004ControllerEnvelopeInfo.java)
   The example lists the basic information about an envelope, including its overall status.
1. **List an envelope's recipients and their current status.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG005ControllerEnvelopeRecipients.java)
1. **List an envelope's documents.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG006ControllerEnvelopeDocs.java)
1. **Download an envelope's documents.** The example can download individual
   documents, the documents concatenated together, or a zip file of the documents.
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG007ControllerEnvelopeGetDoc.java)
1. **Programmatically create a template.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG008ControllerCreateTemplate.java)
1. **Send an envelope using a template.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG009ControllerUseTemplate.java)
1. **Send an envelope and upload its documents with multipart binary transfer.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG010ControllerSendBinaryDocs.java)
   Binary transfer is 33% more efficient than using Base64 encoding.
1. **Embedded sending.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG011ControllerEmbeddedSending.java)
1. **Embedded DocuSign web tool (NDSE).**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG012ControllerEmbeddedConsole.java)
1. **Embedded Signing from a template with an added document.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG013ControllerAddDocToTemplate.java)
   This example sends an envelope based on a template.
   In addition to the template's document(s), the example adds an
   additional document to the envelope by using the
   [Composite Templates](https://developers.docusign.com/esign-rest-api/guides/features/templates#composite-templates)
   feature.
1. **Payments example: an order form, with online payment by credit card.**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG014ControllerCollectPayment.java)
1. **Get the envelope tab data.**
   Retrieve the tab (field) values for all of the envelope's recipients.
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG015ControllerGetTabValues.java)
1. **Set envelope tab values.**
   The example creates an envelope and sets the initial values for its tabs (fields). Some of the tabs
   are set to be read-only, others can be updated by the recipient. The example also stores
   metadata with the envelope.
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG016ControllerSetTabValues.java)
1. **Set template tab values.**
   The example creates an envelope using a template and sets the initial values for its tabs (fields).
   The example also stores metadata with the envelope.
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG017ControllerSetTemplateTabValues.java)
1. **Get the envelope custom field data (metadata).**
   The example retrieves the custom metadata (custom data fields) stored with the envelope.
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG018ControllerEnvelopeCustomFieldValues.java)
1. **Requiring an Access Code for a Recipient**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG019ControllerAccessCodeAuthentication.java)
   This example sends and envelope that requires an access-code for the purpose of multi-factor authentication.
1. **Requiring SMS authentication for a recipient**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG020ControllerSmsAuthentication.java)
   This example sends and envelope that requires entering in a six digit code from an text message for the purpose of multi-factor authentication.
1. **Requiring Phone authentication for a recipient**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG021ControllerPhoneAuthentication.java)
   This example sends and envelope that requires entering in a voice-based response code for the purpose of multi-factor authentication.
1. **Requiring Knowledge-Based Authentication (KBA) for a Recipient**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG022ControllerKBAAuthentication.java)
   This example sends and envelope that requires passing a Public records check to validate identity for the purpose of multi-factor authentication.
1. **Requiring ID Verification (IDV) for a recipient** 
    [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG023ControllerIdvAuthentication.java)
    This example sends and envelope that requires the recipient to upload a government issued id.  
1. **Creating a permission profile**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG024ControllerPermissionCreate.java)
   This code example demonstrates how to create a user group's permission profile using the [Create Profile](https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/create) method. 
1. **Setting a permission profile**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG025ControllerPermissionSetUserGroups.java)
   This code example demonstrates how to set a user group's permission profile using the [Update Group](https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/update) method. 
   You must have already created permissions profile and group of users.
1. **Updating individual permission settings** 
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG026ControllerPermissionChangeSingleSetting.java)
   This code example demonstrates how to update individual settings for a specific permission profile using the [Update Permission Profile](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/update) method.
   You must have already created permissions profile and group of users.
1. **Deleting a permission profile**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG027ControllerPermissionDelete.java)
   This code example demonstrates how to an account's permission profile using the [Delete AccountPermissionProfiles](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/delete) method. 
1. **Creating a brand**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG028ControllerCreateBrand.java)
   This example creates brand profile for an account using the [Create Brand](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountBrands/create) method.
1. **Applying a brand to an envelope**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG029ControllerApplyBrandToEnvelope.java)
   This code example demonstrates how to apply a brand you've created to an envelope using the [Create Envelope](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. 
   First, creates the envelope and then applies brand to it.
   Anchor text ([AutoPlace](https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience)) is used to position the signing fields in the documents.
1. **Applying a brand to a template**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG030ControllerApplyBrandToTemplate.java)
   This code example demonstrates how to apply a brand you've created to a template using using the [Create Envelope](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. 
   You must have at least one created template and brand.
 
1. **Bulk sending envelopes to multiple recipients**
   [Source.](./src/main/java/com/docusign/controller/eSignature/examples/EG031ControllerBulkSendEnvelopes.java)
   This example creates and sends a bulk envelope by generating a bulk recipient list and initiating a bulk send.



## Included OAuth grant types:

* Authentication with Docusign via [Authorization Code Grant flow](https://developers.docusign.com/platform/auth/authcode) .
When the token expires, the user is asked to re-authenticate.
The **refresh token** is not used in this example.

* Authentication with DocuSign via the [JSON Web Token (JWT) Grant](https://developers.docusign.com/platform/auth/jwt/).
When the token expires, it updates automatically.


## Installation

### Prerequisites
**Note: If you downloaded this code using Quickstart from the DocuSign Developer Center, skip steps 1 and 2 below as they're automatically performed for you.**

1. A DocuSign developer account (email and password) on [demo.docusign.net](https://demo.docusign.net).
   Create a [free account](https://go.docusign.com/sandbox/productshot/?elqCampaignId=16533).
1. A DocuSign Integration Key (a client ID) that is configured to use the
   OAuth Authorization Code flow.
   You will need the **Integration Key** itself, and its **secret**. To
   use JSON Web token, you will need the **Integration Key** itself, the
   **RSA Secret Key** and an API user ID for the user you are impersonating.  

   If you use this launcher on your own workstation,
   the Integration key must include following **Redirect URI**s:
   * http://localhost:8080/login&type=acg
   * http://localhost:8080/login&type=jwt

   If you will not be running the launcher on your own workstation,
   use the appropriate DNS name and port instead of `localhost:8080`.
   A sample Redirect URI: http://myserver.it.mycompany.com/login

1. [JDK 11](https://jdk.java.net/14/) or later
1. [Maven](https://maven.apache.org/download.cgi)
1. A name and email for a signer, and a name and email for a cc recipient.
   The signer and the cc email cannot be the same.


### Short installation instructions
* Download or clone this repository.
* The project includes a Maven pom file.   

**Note: If you downloaded this code using Quickstart from the DocuSign Developer Center, skip the next step as it was automatically performed for you.**
* Configure the project by overriding necessary properties from the `src\main\resources\application.example.json` and saving this file as `application.json` file. **Don't add this file into the Git index.**
* Add VM argument `-Dspring.profiles.active=dev` to your IDE
* Note that IntelliJ IDEA Community Edition does not directly support
  Spring Boot applications.  

### Build and run
Launchers are built as a dedicated application with embedded TomCat server. Build:  
``` bash
$ cd code-examples-java
$ mvn package
```
Run:  
``` bash
$ cd target
$ java -Dspring.profiles.active=dev -jar code-examples-java-1.0-SNAPSHOT.war
```


## IntelliJ IDEA Ultimate instructions

The [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/)
can be used with the launcher. The IntelliJ IDEA Ultimate is
required due to its support for Spring Boot and JSP view pages.

**Note: If you downloaded this code using Quickstart from the DocuSign Developer Center, skip Step 1 as it was automatically performed for you.**

**Step 1.** Download or clone the
[code-examples-java](https://github.com/docusign/code-examples-java)
   repository

**Step 2.** Start IntelliJ IDEA Ultimate and choose the **Open or Import** option.

![IntelliJ Import project](./docs/IntelliJ_import_highlight.jpg)

**Step 3.** Use the popup file chooser to select 
**code-examples-java** or your unzipped Quickstart directory.

**Step 4.** Click **OK** and the project will
be displayed in the IDE.

### Configuring IntelliJ's *Run/Debug Configuration*
IntelliJ uses **Run/Debug Configuration** to manage
settings for running the launcher.

Set up a Run/Debug Configuration for the launcher:

Step 1. Use the menu command **Run > Edit configurations...**
to open the configuration manager.

Step 2. Under **Templates** in the left sidebar, select **Spring Boot**.

Step 3. Update the form with the **Name** of the
configuration to `code-examples-java` and the **Main class** for the configuration,
`com.docusign.App` **Tip:** Use the **...** (ellipses) button next to the field to choose the Main class.

Under **Spring Boot** select the `Enable debug output` checkbox.

![Configure a Run configuration](./docs/install_fig_7.png)

### Running or debugging the launcher

Use a **Run** menu option to run or debug the launcher.

After the application finishes building, open your
browser to http://localhost:8080



## Eclipse instructions

**Note: If you downloaded this code using Quickstart from the DocuSign Developer Center, skip the first step as it was automatically performed for you.**

* Download or clone this repository.
* Open Eclipse and select import. When the window appears, click the Maven folder, then click `Existing Maven Project` and choose `Next`.
* For the Root directory, Browse for and select this same repo `code-examples-java`.
* Once selected, click `Finish` at the bottom center to save the project link to your eclipse workspace.
* Next, click the menu item `Run->Run Configurations` and choose a new Maven configuration with the following:
  * Name the configuration `code-examples-java`.
  * Put in the following value for your workspace in the Base Directory: `${workspace_loc:/code-examples-java}`.
  * Put in the following value for the Goals field: `spring-boot::run`.
* Click the JRE tab in the Run Configurations menu and make sure your have a Runtime JRE set to jdk-11 or higher.
* Click apply to save. Finally, click `Run` to run it!
* Open a browser to http://localhost:8080


### Payments code example
To use the payments example, create a
test payments gateway for your developer account.

See the
[PAYMENTS_INSTALLATION.md](https://github.com/docusign/code-examples-java/blob/master/PAYMENTS_INSTALLATION.md)
file for instructions.

Then add the payment gateway account id to the **application.properties** file.


## License and additional information

### License
This repository uses the MIT License. See the LICENSE file for more information.

### Pull Requests
Pull requests are welcomed. Pull requests will only be considered if their content
uses the MIT License.

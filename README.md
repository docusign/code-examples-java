# Java Launcher Code Examples

### Github repo: [code-examples-java](./)
## Introduction
This repo is a Java Spring Boot application that demonstrates:

1. **Embedded Signing Ceremony.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG001ControllerEmbeddedSigning.java)
   This example sends an envelope, and then uses an embedded signing ceremony for the first signer.
   With embedded signing, the DocuSign signing ceremony is initiated from your website.
1. **Send an envelope with a remote (email) signer and cc recipient.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG002ControllerSigningViaEmail.java)
   The envelope includes a pdf, Word, and HTML document.
   Anchor text ([AutoPlace](https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience)) is used to position the signing fields in the documents.
1. **List envelopes in the user's account.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG003ControllerListEnvelopes.java)
1. **Get an envelope's basic information.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG004ControllerEnvelopeInfo.java)
   The example lists the basic information about an envelope, including its overall status.
1. **List an envelope's recipients and their current status.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG005ControllerEnvelopeRecipients.java)
1. **List an envelope's documents.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG006ControllerEnvelopeDocs.java)
1. **Download an envelope's documents.** The example can download individual
   documents, the documents concatenated together, or a zip file of the documents.
   [Source.](./src/main/java/com/docusign/controller/examples/EG007ControllerEnvelopeGetDoc.java)
1. **Programmatically create a template.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG008ControllerCreateTemplate.java)
1. **Send an envelope using a template.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG009ControllerUseTemplate.java)
1. **Send an envelope and upload its documents with multipart binary transfer.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG010ControllerSendBinaryDocs.java)
   Binary transfer is 33% more efficient than using Base64 encoding.
1. **Embedded sending.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG011ControllerEmbeddedSending.java)
1. **Embedded DocuSign web tool (NDSE).**
   [Source.](./src/main/java/com/docusign/controller/examples/EG012ControllerEmbeddedConsole.java)
1. **Embedded Signing Ceremony from a template with an added document.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG013ControllerAddDocToTemplate.java)
   This example sends an envelope based on a template.
   In addition to the template's document(s), the example adds an
   additional document to the envelope by using the
   [Composite Templates](https://developers.docusign.com/esign-rest-api/guides/features/templates#composite-templates)
   feature.
1. **Payments example: an order form, with online payment by credit card.**
   [Source.](./src/main/java/com/docusign/controller/examples/EG014ControllerCollectPayment.java)
1. **Get the envelope tab data.**
   Retrieve the tab (field) values for all of the envelope's recipients.
   [Source.](./src/main/java/com/docusign/controller/examples/EG015ControllerGetTabValues.java)
1. **Set envelope tab values.**
   The example creates an envelope and sets the initial values for its tabs (fields). Some of the tabs
   are set to be read-only, others can be updated by the recipient. The example also stores
   metadata with the envelope.
   [Source.](./src/main/java/com/docusign/controller/examples/EG016ControllerSetTabValues.java)
1. **Set template tab values.**
   The example creates an envelope using a template and sets the initial values for its tabs (fields).
   The example also stores metadata with the envelope.
   [Source.](./src/main/java/com/docusign/controller/examples/EG017ControllerSetTemplateTabValues.java)
1. **Get the envelope custom field data (metadata).**
   The example retrieves the custom metadata (custom data fields) stored with the envelope.
   [Source.](./src/main/java/com/docusign/controller/examples/EG018ControllerEnvelopeCustomFieldValues.java)
1. **Requiring an Access Code for a Recipient**
   [Source.](./src/main/java/com/docusign/controller/examples/EG019ControllerAccessCodeAuthentication.java)
   This example sends and envelope that requires an access-code for the purpose of multi-factor authentication.
1. **Requiring SMS authentication for a recipient**
   [Source.](./src/main/java/com/docusign/controller/examples/EG020ControllerSmsAuthentication.java)
   This example sends and envelope that requires entering in a six digit code from an text message for the purpose of multi-factor authentication.
1. **Requiring Phone authentication for a recipient**
   [Source.](./src/main/java/com/docusign/controller/examples/EG021ControllerPhoneAuthentication.java)
   This example sends and envelope that requires entering in a voice-based response code for the purpose of multi-factor authentication.
1. **Requiring Knowledge-Based Authentication (KBA) for a Recipient**
   [Source.](./src/main/java/com/docusign/controller/examples/EG022ControllerKBAAuthentication.java)
   This example sends and envelope that requires passing a Public records check to validate identity for the purpose of multi-factor authentication.
1. **Requiring ID Verification (IDV) for a recipient** 
    [Source.](./src/main/java/com/docusign/controller/examples/EG023ControllerIdvAuthentication.java)
    This example sends and envelope that requires the recipient to upload a government issued id.  
 **Creating a permission profile**
   [Source.](./src/main/java/com/docusign/controller/examples/EG024ControllerPermissionCreate.java)
   This code example demonstrates how to create a user group's permission profile using the [Create Profile](https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/create) method. 
1. **Setting a permission profile**
   [Source.](./src/main/java/com/docusign/controller/examples/EG025ControllerPermissionSetUserGroups.java)
   This code example demonstrates how to set a user group's permission profile using the [Update Group](https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/update) method. 
   You must have already created permissions profile and group of users.
1. **Updating individual permission settings** 
   [Source.](./src/main/java/com/docusign/controller/examples/EG026ControllerPermissionChangeSingleSetting.java)
   This code example demonstrates how to update individual settings for a specific permission profile using the [Update Permission Profile](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/update) method.
   You must have already created permissions profile and group of users.
1. **Deleting a permission profile**
   [Source.](./src/main/java/com/docusign/controller/examples/EG027ControllerPermissionDelete.java)
   This code example demonstrates how to an account's permission profile using the [Delete AccountPermissionProfiles](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/delete) method. 
1. **Creating a brand**
   [Source.](./src/main/java/com/docusign/controller/examples/EG028ControllerCreateBrand.java)
   This example creates brand profile for an account using the [Create Brand](https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountBrands/create) method.
1. **Applying a brand to an envelope**
   [Source.](./src/main/java/com/docusign/controller/examples/EG029ControllerApplyBrandToEnvelope.java)
   This code example demonstrates how to apply a brand you've created to an envelope using the [Create Envelope](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. 
   First, creates the envelope and then applies brand to it.
   Anchor text ([AutoPlace](https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience)) is used to position the signing fields in the documents.
1. **Applying a brand to a template**
   [Source.](./src/main/java/com/docusign/controller/examples/EG030ControllerApplyBrandToTemplate.java)
   This code example demonstrates how to apply a brand you've created to a template using using the [Create Envelope](https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create) method. 
   You must have at least one created template and brand.
 
1. **Bulk sending envelopes to multiple recipients**
   [Source.](./src/main/java/com/docusign/controller/examples/EG031ControllerBulkSendEnvelopes.java)
   This example creates and sends a bulk envelope by generating a bulk recipient list and initiating a bulk send.



## Included OAuth grant types:

* Authentication with Docusign via [Authorization Code Grant flow](https://developers.docusign.com/esign-rest-api/guides/authentication/oauth2-code-grant) .
When the token expires, the user is asked to re-authenticate.
The **refresh token** is not used in this example.

* Authentication with DocuSign via the [JSON Web Token (JWT) Grant](https://developers.docusign.com/esign-rest-api/guides/authentication/oauth2-jsonwebtoken).
When the token expires, it updates automatically.


## Installation

### Prerequisites
1. A DocuSign Developer Sandbox account (email and password) on [demo.docusign.net](https://demo.docusign.net).
   Create a [free account](https://go.docusign.com/sandbox/productshot/?elqCampaignId=16533).
1. A DocuSign Integration Key (a client ID) that is configured to use the
   OAuth Authorization Code flow.
   You will need the **Integration Key** itself, and its **secret**. To
   use JSON Web token, you will need the **Integration Key** itself, the
   **RSA Secret Key** and an API user ID for the user you are impersonating.  

   If you use this example on your own workstation,
   the Integration key must include following **Redirect URI**s:
   * http://localhost:8080/login&type=acg
   * http://localhost:8080/login&type=jwt

   If you will not be running the example on your own workstation,
   use the appropriate DNS name and port instead of `localhost:8080`.
   An example Redirect URI: http://myserver.it.mycompany.com/login

1. Java 11.
1. A name and email for a signer, and a name and email for a cc recipient.
   The signer and the cc email cannot be the same.
1. Maven

### Short installation instructions
* Download or clone this repository.
* The project includes a Maven pom file.
* Configure the project by overriding necessary properties from the `src\main\resources\application.example.json` and saving this file as `application.json` file. **Don't add this file into the Git index.**.
* Add VM argument `-Dspring.profiles.active=dev` to your IDE
* Note that IntelliJ Community Edition does not directly support
  Spring Boot applications.  

### Build and run
Examples are built as a dedicated application with embedded TomCat server. Build:  
``` bash
$ cd code-examples-java
$ mvn package
```
Run:  
``` bash
$ cd target
$ java -Dspring.profiles.active=dev -jar code-examples-java-1.0-SNAPSHOT.war
```

### IntelliJ Ultimate installation

See the [IntelliJ Ultimate instructions](https://github.com/docusign/code-examples-java/blob/master/docs/Readme_IntelliJ_Ultimate.md).

## Configure the example

Configure the example via the properties file:
`code-examples-java/src/main/resources/application-dev.properties`.

Add the client id, secret, signer name and email to the file.
Also add the URL that the application will use (the **DS_APP_URL** setting).
By default, this is http://localhost:8080

You must also add a **Redirect URI** to the client id's settings in
DocuSign. The Redirect URIs are `/login&type=acg` and `/login&type=jwt` appended to the DS_APP_URL setting.
Eg http://localhost:8080/login&type=acg

### Payments code example
To use the payments example, create a
test payments gateway for your developer sandbox account.

See the
[PAYMENTS_INSTALLATION.md](https://github.com/docusign/code-examples-java/blob/master/PAYMENTS_INSTALLATION.md)
file for instructions.

Then add the payment gateway account id to the **application.properties** file.

## Using the examples with other authentication flows

The examples in this repository can also be used with the
JWT OAuth flow.
See the [Authentication guide](https://developers.docusign.com/esign-rest-api/guides/authentication)
for information on choosing the right authentication flow for your application.

## License and additional information

### License
This repository uses the MIT License. See the LICENSE file for more information.

### Pull Requests
Pull requests are welcomed. Pull requests will only be considered if their content
uses the MIT License.

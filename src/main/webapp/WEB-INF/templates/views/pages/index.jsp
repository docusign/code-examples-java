<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<c:if test="${locals.user == null}">
  <!-- IF not signed in -->
  <div class="jumbotron">
    <h1 class="display-4">Welcome!</h1>
    <p class="Xlead">Welcome to the DocuSign Java Example Launcher
      using OAuth2.</p>
  </div>
</c:if>

<div id="index-page">
  <h2>Welcome</h2>
  <p>This launcher both demonstrates use of common OAuth2 grant flows and multiple usage examples from the DocuSign eSignature REST API.</p>
    <c:if test="${showDoc == true}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> on using OAuth Authorization Code Grant from a Java application.</p>
    </c:if>

  <h2>Basic Examples</h2>

  <h4 id="example001">1. <a href="eg001">Embedded Signing Ceremony</a></h4>
  <p>This example sends an envelope, and then uses an embedded signing ceremony for the first signer.
    With embedded signing, the DocuSign signing ceremony is initiated from your website.
  </p>
  <p>API methods used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createRecipient">EnvelopeViews::createRecipient</a>.
  </p>

  <h4 id="example002">2. <a href="eg002">Send an envelope with a remote (email) signer and cc recipient</a></h4>
  <p>The envelope includes a pdf, Word, and HTML document. Anchor text
    (<a target ='_blank' rel="noopener noreferrer" href="https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience">AutoPlace</a>)
    is used to position the signing fields in the documents.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example003">3. <a href="eg003">List envelopes in the user's account</a></h4>
  <p>List the envelopes created in the last 30 days.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/listStatusChanges">Envelopes::listStatusChanges</a>.
  </p>

  <h4 id="example004">4. <a href="eg004">Get an envelope's basic information and status</a></h4>
  <p>List the basic information about an envelope, including its overall status.
    Additional API/SDK methods may be used to get additional information about the
    envelope, its documents, recipients, etc.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/get">Envelopes::get</a>.
  </p>

  <h4 id="example005">5. <a href="eg005">List an envelope's recipients and their status</a></h4>
  <p>List the envelope's recipients, including their current status.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeRecipients/list">EnvelopeRecipients::list</a>.
  </p>

  <h4 id="example006">6. <a href="eg006">List an envelope's documents</a></h4>
  <p>List the envelope's documents. A <em>Certificate of Completion</em> document
    is also associated with every envelope.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeDocuments/list">EnvelopeDocuments::list</a>.
  </p>

  <h4 id="example007">7. <a href="eg007">Download a document from an envelope</a></h4>
  <p>An envelope's documents can be downloaded one by one or as a complete set.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeDocuments/get">EnvelopeDocuments::get</a>.
  </p>

  <h4 id="example008">8. <a href="eg008">Create a template</a></h4>
  <p>Create a template with two roles, <strong>signer</strong> and <strong>cc</strong>.</p>
  <p>API methods used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Templates/Templates/list">Templates::list</a>,
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Templates/Templates/create">Templates::create</a>.
  </p>

  <h4 id="example009">9. <a href="eg009">Send an envelope using a template</a></h4>
  <p>The envelope is defined by the template.
    The signer and cc recipient name and email are used to fill in the template's <em>roles</em>.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example010">10. <a href="eg010">Send an envelope using binary document transfer</a></h4>
  <p>The envelope includes a pdf, Word, and HTML document.</p>
  <p>Multipart data transfer is used to send the documents in binary format to DocuSign.
    Binary transfer is 33% more efficient than base64 encoding and is recommended for documents over 15M Bytes.
    Binary transfer is not yet supported by the SDK.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example011">11. <a href="eg011">Embedded sending</a></h4>
  <p>An envelope will be created in draft mode. The DocuSign
    web tool (NDSE) will then be shown, enabling further updates
    to the envelope before it is sent.
  </p>
  <p>API methods used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>,
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createSender">EnvelopeViews::createSender</a>.

  <h4 id="example012">12. <a href="eg012">Embedded DocuSign web tool</a></h4>
  <p>Redirect the user to the DocuSign web tool.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createConsole">EnvelopeViews::createConsole</a>.
  </p>

  <h4 id="example013">13. <a href="eg013">Embedded Signing Ceremony from a template with an added document</a></h4>
  <p>This example sends an envelope based on a template.</p>
  <p>In addition to the template's document(s), the example adds an
    additional document to the envelope by using the
    <a target='_blank' rel="noopener noreferrer" href='https://developers.docusign.com/esign-rest-api/guides/features/templates#composite-templates'>Composite Templates</a>
    feature.</p>
  <p>API methods used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createRecipient">EnvelopeViews::createRecipient</a>.
  </p>

  <h4 id="example014">14. <a href="eg014">Send an envelope with an order form and payment field</a></h4>
  <p>Anchor text
    (<a target ='_blank' rel="noopener noreferrer" href="https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience">AutoPlace</a>)
    is used to position the fields in the documents.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>



  <h2>Tabs Examples</h2>
  <h4 id="example015">15. <a href="eg015">Get the tab data from an envelope</a></h4>
  <p>This example retrieves the <strong>tab</strong> (<a target="_blank" href="https://developers.docusign.com/esign-rest-api/guides/concepts/tabs">field</a>) values from an envelope.</p>
  <p>
    API method used:
    <a target='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeFormData/get">EnvelopeFormData::get</a>.
  </p>

  <h4 id="example016">16. <a href="eg016">Set tab values for an envelope</a></h4>
  <p>This example sets the tab (field) values for an envelope including tabs that can and cannot be changed by the signer.</p>
  <p>
    API method used:
    <a target='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a> and
    <a target ='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createRecipient">EnvelopeViews::createRecipient</a>.
  </p>

  <h4 id="example017">17. <a href="eg017">Set template tab values</a></h4>
  <p>This example sets the tab (field) values for a template being used by an envelope.</p>
  <p>
    API method used:
    <a target='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a> and
    <a target ='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createRecipient">EnvelopeViews::createRecipient</a>.
  </p>

  <h4 id="example018">18. <a href="eg018">List envelope custom metadata field values</a></h4>
  <p>This example lists the envelope's custom metadata field values.</p>
  <p>
    API method used:
    <a target='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeCustomFields/list">EnvelopeCustomFields::list</a>.
  </p>


  <h4 id="example019">19. <a href="eg019">Send an envelope with Access Code Authentication</a></h4>
  <p>This example sends and envelope that requires an access-code for the purpose of multi-factor authentication.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example020">20. <a href="eg020">Send an envelope with SMS Authentication</a></h4>
  <p>This example sends and envelope that requires entering in a six digit code from an text message for the purpose of multi-factor authentication.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example021">21. <a href="eg021">Send an envelope with Phone Authentication</a></h4>
  <p>This example sends and envelope that requires entering in a voice-based response code for the purpose of multi-factor authentication.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example022">22. <a href="eg022">Send an envelope with Knowledge-Based Authentication</a></h4>
  <p>This example sends and envelope that requires passing a Public records check to validate identity for the purpose of multi-factor authentication.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example023">23. <a href="eg023">Send an envelope with ID Verification Authentication</a></h4>
  <p>Submit an envelope that requires verification of a government issued identity.</p>
  <p>
    API method used:
    <a target ='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>
  
    <h4 id="example024">24. Creating a Permission Profile</h4>
  <p>
    Coming Soon
  </p>
 
  <h4 id="example025">25. <a href="eg025">Setting a permission profile</a></h4>
  <p>This example allows you to set a permission profile on an existing user group.</p>
  <p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/UserGroups/Groups/update">Groups::update</a>.
  </p>

    <h4 id="example026">26. Modify a Permission Profile</h4>
  <p>
    Coming Soon
  </p>
  
  <h4 id="example027">27. <a href="eg027">Deleting a permission profile</a></h4>
  <p>This example lists all available permissions profiles and allows you to delete any without associated users. Please note that you cannot remove "Everyone" nor "Administrator" permission profiles.</p>
  <p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/delete">AccountPermissionProfiles::create</a>.
  </p>

  <h4 id="example028">28. <a href="eg028">Create a brand</a></h4>
  <p>This example will create an account brand that can be used to apply customization to your envelopes such as your own logo, colors, and text elements.</p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountBrands/create">AccountBrands::create</a>.
  </p>
  
  <h4 id="example029">29. <a href="eg029">Applying a brand to an envelope</a></h4>
  <p>This example will show you how to create an envelope using any of the created brands on your account.</p>
  <p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example030">30. <a href="eg030">Applying a brand to a template</a></h4>
  <p>This example will show you how to create an envelope using a brand </p>
  <p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
  </p>

  <h4 id="example031">31. Bulk sending envelopes to multiple recipients</h4>
  <p>
	Coming Soon
  </p>
</div>

<!-- anchor-js is only for the index page -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page="../partials/foot.jsp"/>

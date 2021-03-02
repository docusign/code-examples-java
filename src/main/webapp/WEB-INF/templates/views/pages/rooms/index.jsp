<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../partials/head.jsp"/>

<c:if test="${locals.user == null}">
  <!-- IF not signed in -->
  <div>
  <div class="jumbotron jumbotron-fluid"> <table>
    <tbody>
    <tr>
    <td>
      <h1 class="display-4">Java Launcher</h1>
    <p class="Xlead">Welcome to the DocuSign Java examples using multiple OAuth flows (JWT and Authorization Code Grant).</p>
    </td>
    <td>
        <img src="/assets/banner-code.png" />
    </td>
  </tr>
  </tbody>
  </table>
</div>
</c:if>

<div class="container" style="margin-top: 40px" id="index-page">
  <h2>Welcome</h2>
  <p>This launcher both demonstrates use of common OAuth2 grant flows and multiple usage examples from the DocuSign Rooms REST API.</p>
    <c:if test="${showDoc == true}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> on using OAuth Authorization Code Grant from a Java application.</p>
    </c:if>

  <h2>Rooms Examples</h2>

  <h4 id="example001">1. <a href="r001">Creating a room with data</a></h4>
  <p>This example demonstrates creating a DocuSign room.
  </p>
  <p>API methods used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Roles/Roles/GetRoles">Roles::GetRoles</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/CreateRoom">Rooms::CreateRoom</a>.
  </p>

  <h4 id="example002">2. <a href="r002">Creating a room with a template</a></h4>
  <p>This example demonstrates creating a DocuSign Room using a predefined template.
    If you've <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides/templates">created a template</a>
    for your organization in either Rooms for Mortgage or Rooms for Real Estate,
    you can create rooms based on this template using the Rooms API.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/RoomTemplates/RoomTemplates/GetRoomTemplates">RoomTemplates::GetRoomTemplates</a>,
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Roles/Roles/GetRoles">Roles::GetRoles</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/CreateRoom">Rooms::CreateRoom</a>.
  </p>

  <h4 id="example003">3. <a href="r003">Exporting data from a room</a></h4>
  <p>This example demonstrates how to export rooms data from a<a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides">DocuSign Room</a>.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRoomFieldData">Rooms::GetRoomFieldSet</a>.
  </p>

  <h4 id="example004">4. <a href="r004">Adding forms to a room</a></h4>
  <p>This example demonstrates how to attach forms to a room using the Rooms API.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a>,
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Forms/FormLibraries/GetFormLibraries">FormLibraries::GetFormLibraryForms</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/AddFormToRoom">Rooms::AddFormToRoom</a>.
  </p>

  <h4 id="example005">5. <a href="r005">Get Rooms with filters</a></h4>
  <p>This example demonstrates how to return rooms filtered by your parameters using the
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides">Rooms API</a>.
    This specific code example filters for all rooms that have had their field data, updated within
    the last 10 days, as shown on the Details tab in the UI.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a>.
  </p>

  <h4 id="example006">6. <a href="r006">Creating an external form fill session</a></h4>
  <p>The DocuSign Rooms API offers developers the option to have users fill out forms without
    logging in to the Rooms UI through the use of an <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides/forms">external form fill session</a>.
    This example demonstrates how to create an external form fill session using the
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides">Rooms API</a>:
    the result of this code example is the URL for the form fill session, which you can embed in
    your integration or send to the user.
  </p>
  <p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a>,
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Forms/FormLibraries/GetFormLibraries">FormLibraries::GetFormLibraries</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Forms/ExternalFormFillSessions/CreateExternalFormFillSession">ExternalFormFillSessions::CreateExternalFormFillSession</a>,
  </p>

  <h4 id="example007">7. <a href="r007">Creating a form group</a></h4>
  <p>This example demonstrates creating a form group for your DocuSign
    Rooms for Real Estate account.
  </p>
  <p>API method used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/createformgroup">FormGroups:CreateFormGroup</a>.
  </p>

  <h4 id="example008">8. <a href="r008">Granting office access to a form group</a></h4>
  <p>This example demonstrates how to assign an office to
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/rooms101/forms/managing-forms-rooms/">a form group</a>
    for your DocuSign Rooms for Real Estate account. Granting office access to a form group will
    enable you to filter which form groups are available based on that office.
  </p>
  <p>API method used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/grantofficeaccesstoformgroup">FormGroups:GrantOfficeAccessToFormGroup</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/getformgroups">FormGroups:GetFormGroups</a> and
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/offices/offices/getoffices">Offices:GetOffices</a>.
  </p>

  <h4 id="example009">9. <a href="r009">Assigning a form to a form group</a></h4>
  <p>This example demonstrates how to assign a form to
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/rooms101/forms/managing-forms-rooms/">a form group</a>
    for your DocuSign Rooms for Real Estate account. As a prerequisite, ensure that you have
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/how-to/create-form-group/">created a form group</a>
    and <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/how-to/access-form-group/">set the office ID on this form group</a>
    before proceeding.
  </p>
  <p>API method used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/assignformgroupform">FormGroups:AssignFormGroupForm</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/getformgroups">FormGroups:GetFormGroups</a> and
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Forms/FormLibraries/GetFormLibraries">FormLibraries::GetFormLibraryForms</a>.
  </p>
</div>

<!-- anchor-js is only for the index page -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page="../../partials/foot.jsp"/>

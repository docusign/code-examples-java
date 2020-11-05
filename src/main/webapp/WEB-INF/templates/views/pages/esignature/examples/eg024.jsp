<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>24. ${title}.</h4>

<p>
Permission profiles are collections of account settings that determine the behavior and actions 
available to the user groups to which they're applied. This code example demonstrates how to 
create a permission profile with the eSignature REST API.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/create">AccountPermissionProfiles::create</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="permissionProfileName">Profile Name</label>
        <input type="text" class="form-control" id="permissionProfileName" name="permissionProfileName" value="${permissionProfileName}" required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
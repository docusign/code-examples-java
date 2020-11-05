<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>27. ${title}.</h4>

<p>
    Permission profiles are collections of account settings that determine the behavior and actions
    available to the user groups to which they're applied. This code example demonstrates how to
    delete a permission profile with the eSignature REST API.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/delete">AccountPermissionProfiles::update</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="profileId">Select permission profile to update</label>
        <select id="profileId" name="profileId" class="form-control">
            <c:forEach items="${listProfiles}" var="profile">
                <option value="${profile.permissionProfileId}">${profile.permissionProfileName}</option>
            </c:forEach>
        </select>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

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
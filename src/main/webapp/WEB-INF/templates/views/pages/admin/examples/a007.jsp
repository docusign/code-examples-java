<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>7. Retrieve the user's DocuSign profile using a User ID</h4>
<p>
    Demonstrates how to obtain the user's DocuSign profile information across all DocuSign accounts by specifying the user's User ID.
</p>

<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>
    API method used:
    <a target="_blank" rel='noopener noreferrer'
       href="https://developers.docusign.com/docs/admin-api/reference/usermanagement/esignusermanagement/getuserprofiles/">MultiProductUserManagement:getUserDSProfile</a>
</p>

<p>
    View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<form class="eg" action="" method="post" data-busy="form">
<div class="form-group">
    <label for="userId">User ID</label>
    <input type="text" class="form-control" id="userId" name="userId"
           placeholder="00000000-0000-0000-0000-000000000000" required
           pattern="[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}">

    <br/>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-primary">Continue</button>
</div>
</form>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>4. Add users via bulk import</h4>
<p>Demonstrates how to bulk import users and add user data from an example CSV (comma-separated value) file into a DocuSign Admin organization.</p>
<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API methods used:
    <a target='_blank' rel='noopener noreferrer' 
    href="https://developers.docusign.com/docs/admin-api/reference/bulkoperations/userimport/addbulkuserimport/">UserImport:addBulkUserImport</a>, 
<a target='_blank' rel='noopener noreferrer'
    href='https://developers.docusign.com/docs/admin-api/reference/bulkoperations/userimport/getbulkuserimportrequest/'>UserImport:getBulkUserImportRequest</a>.
</p>

<p>
    View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<form class='eg' action='' method='post' data-busy='form'>
    <input type='hidden' name='_csrf' value='${csrfToken}'>
    <button type='submit' class='btn btn-docu'>Continue</button>
</form>

<jsp:include page='../../../partials/foot.jsp'/>

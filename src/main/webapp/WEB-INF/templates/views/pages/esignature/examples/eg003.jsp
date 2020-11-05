<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>3. List envelopes in the user's account</h4>
<p>List the envelopes created in the last 30 days.</p>
<p>This example demonstrates how to query DocuSign about envelopes sent by the current user.</p>
<c:if test="${showDoc}">
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>API method used:
    <a target='_blank' rel="noopener noreferrer"
       href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/listStatusChanges">Envelopes::listStatusChanges</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Continue</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>8. Create a template</h4>
<p>Create a template with two roles, <strong>signer</strong> and <strong>cc</strong>.
    The template includes three documents.
</p>

<p>Templates are usually created using the DocuSign web tool. This example creates
    a template that will later be used by other examples in this launcher. </p>

<p>This example first lists the user's templates and only creates a new template if one does not already exist in your
    account.
</p>
<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>API methods used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Templates/Templates/list">Templates::list</a>,
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Templates/Templates/create">Templates::create</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Continue</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

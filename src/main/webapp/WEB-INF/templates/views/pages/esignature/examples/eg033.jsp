<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>33. ${title}.</h4>

<p>
    This topic demonstrates how to resume an envelope workflow that has been paused.
    For information on creating an envelope with a paused workflow, see
    <a href="https://developers.docusign.com/docs/esign-rest-api/how-to/pause-workflow">
        How to pause a signature workflow
    </a>. The envelope includes a txt document.
</p>

<c:if test="${showDoc}">
    <p><a target="_blank" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API methods used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/envelopes/envelopes/update">Envelopes::update</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
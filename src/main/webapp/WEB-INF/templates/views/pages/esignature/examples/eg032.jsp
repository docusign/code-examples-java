<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>32. ${title}.</h4>

<p>
    This topic demonstrates how to create an envelope where the workflow is paused before the
    envelope is sent to a second recipient. For information on resuming a workflow see
    <a href="https://developers.docusign.com/docs/esign-rest-api/how-to/unpause-workflow">
        How to unpause a signature workflow
    </a>. The envelope includes a txt document.
</p>

<c:if test="${showDoc}">
    <p><a target="_blank" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API methods used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/envelopes/envelopes/create">Envelopes::create</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="signerEmail">Signer Email</label>
        <input type="email" class="form-control" id="signerEmail" name="signerEmail"
               aria-describedby="emailHelp" placeholder="pat@example.com" required
               value="${locals.dsConfig.signerEmail}">
        <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
    </div>
    <div class="form-group">
        <label for="signerName">Signer Name</label>
        <input type="text" class="form-control" id="signerName" placeholder="Pat Johnson" name="signerName"
               value="${locals.dsConfig.signerName}" required>
    </div>

    <div class="form-group">
        <label for="signerEmail2">Second Signer Email</label>
        <input type="email" class="form-control" id="signerEmail2" name="signerEmail2"
               aria-describedby="emailHelp" placeholder="pat@example.com" required
               value="${locals.dsConfig.signerEmail}">
        <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
    </div>
    <div class="form-group">
        <label for="signerName2">Second Signer Name</label>
        <input type="text" class="form-control" id="signerName2" placeholder="Pat Johnson" name="signerName2"
               value="${locals.dsConfig.signerName}" required>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
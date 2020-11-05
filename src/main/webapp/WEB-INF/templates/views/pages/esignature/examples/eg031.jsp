<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>31. ${title}.</h4>

<p>
    This code example demonstrates how to send envelopes in bulk to multiple recipients with the
    eSignature API. To accomplish the task, you will first create a bulk send recipients list,
    and then create an envelope. From there, you will combine the envelope and bulk list to
    initiate bulk send. Method BulkSend::createBulkSendList creates a bulk send list that you can
    use to send an envelope to up to 1,000 recipients at once.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API methods used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeRecipients/create">EnvelopeRecipients::create</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/BulkEnvelopes/BulkEnvelopes/get">BulkEnvelopes::get</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeCustomFields/create">EnvelopeCustomFields::create</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/BulkEnvelopes/BulkSend/createBulkSendList">BulkSend::createBulkSendList</a>
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeRecipients/create">EnvelopeRecipients::create</a>.
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
        <label for="ccEmail">CC Email</label>
        <input type="email" class="form-control" id="ccEmail" name="ccEmail"
               aria-describedby="emailHelp" placeholder="pat@example.com" required>
        <small id="emailHelp" class="form-text text-muted">The email and/or name for the cc recipient must be different
            from the signer.
        </small>
    </div>
    <div class="form-group">
        <label for="ccName">CC Name</label>
        <input type="text" class="form-control" id="ccName" placeholder="Pat Johnson" name="ccName" required>
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
    <div class="form-group">
        <label for="ccEmail2">Second CC Email</label>
        <input type="email" class="form-control" id="ccEmail2" name="ccEmail2"
               aria-describedby="emailHelp" placeholder="pat@example.com" required>
        <small id="emailHelp" class="form-text text-muted">The email and/or name for the cc recipient must be different
            from the signer.
        </small>
    </div>
    <div class="form-group">
        <label for="ccName2">Second CC Name</label>
        <input type="text" class="form-control" id="ccName2" placeholder="Pat Johnson" name="ccName2" required>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${title}.</h4>

<p>
    Demonstrates how to send envelopes in bulk to multiple recipients. First, this example
    creates a bulk-send recipients list, then creates an envelope. After that, it initiates bulk
    envelope sending.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API methods used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/bulkenvelopes/bulksend/createbulksendlist/">BulkSend::createBulkSendList</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/envelopes/envelopes/create/">Envelopes::create</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/envelopes/envelopecustomfields/create/">EnvelopeCustomFields::create</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/bulkenvelopes/bulksend/createbulksendrequest/">BulkSend::createBulkSendRequest</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/esign-rest-api/reference/bulkenvelopes/bulksend/getbulksendbatchstatus/">BulkSend::getBulkSendBatchStatus</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="row">
        <div class="col-md-6">
            <h5>Bulk copy #1</h5>
            <div class="form-group">
                <label for="signerEmail">Signer Email</label>
                <input type="email" class="form-control" id="signerEmail" name="signerEmail"
                       aria-describedby="emailHelp" placeholder="pat@example.com" required
                       value="${locals.dsConfig.signerEmail}">
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
            </div>

            <div class="form-group">
                <label for="ccName">CC Name</label>
                <input type="text" class="form-control" id="ccName" placeholder="Pat Johnson" name="ccName"
                required>
            </div>
        </div>

        <div class="col-md-6">
            <h5>Bulk copy #2</h5>
            <div class="form-group">
                <label for="signerEmail2">Signer Email</label>
                <input type="email" class="form-control" id="signerEmail2" name="signerEmail2"
                       aria-describedby="emailHelp" placeholder="pat@example.com" required
                       value="${locals.dsConfig.signerEmail}">
            </div>

            <div class="form-group">
                <label for="signerName2">Signer Name</label>
                <input type="text" class="form-control" id="signerName2" placeholder="Pat Johnson" name="signerName2"
                       value="${locals.dsConfig.signerName}" required>
            </div>

            <div class="form-group">
                <label for="ccEmail2">CC Email</label>
                <input type="email" class="form-control" id="ccEmail2" name="ccEmail2"
                       aria-describedby="emailHelp" placeholder="pat@example.com" required>
            </div>

            <div class="form-group">
                <label for="ccName2">CC Name</label>
                <input type="text" class="form-control" id="ccName2" placeholder="Pat Johnson" name="ccName2"
                required>
            </div>
        </div>
        </div>
    <div>
        <input type="hidden" name="csrf_token" value="{{ csrf_token() }}"/>
        <button type="submit" class="btn btn-docu">Submit</button>
    </div>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

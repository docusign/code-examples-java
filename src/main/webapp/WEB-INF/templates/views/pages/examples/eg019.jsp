<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../partials/head.jsp"/>

<h4>19. Send an envelope with a recipient using Access Code Authentication.</h4>

<p>
    The envelope includes a pdf document. Anchor text
    (<a href="https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience">AutoPlace</a>)
    is used to position the signing fields in the documents.
</p>
<p>
    This is a general example of creating and sending an envelope (a signing request) using An Access Code for Recipient Authentication.
</p>

<c:if test="${showDoc}">
<p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="accessCode">Access Code</label>
                <input type="text" class="form-control" id="accessCode" name="accessCode"
                       aria-describedby="acText" placeholder="Enter a recipient access code here" required>
                <small id="acText" class="form-text text-muted">Provide this string to a recipient that is different such as in person or by mail or via different email.</small>
            </div>
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
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>

<jsp:include page="../../partials/foot.jsp"/>

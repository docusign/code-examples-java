<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>Set document visibility for envelope recipients</h4>
<p>
    Demonstrates how to set document visibility for envelope recipients.
<p>
<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>${launcherTexts.getAPIMethodUsed()}
    <a target ='_blank' href="https://developers.docusign.com/docs/esign-rest-api/reference/envelopes/envelopes/create/">Envelopes::create</a>.
</p>

<p>
    ${viewSourceFile}
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="signer1Email">Signer1 Email</label>
        <input type="email" class="form-control" id="signer1Email" name="signerEmail"
               aria-describedby="emailHelp" placeholder="pat@example.com" required
               value="${locals.dsConfig.signerEmail}" />
        <small class="form-text text-muted">${launcherTexts.getHelpingTexts().getEmailWontBeShared()}</small>
    </div>
    <div class="form-group">
        <label for="signer1Name">Signer1 Name</label>
        <input type="text" class="form-control" id="signer1Name" placeholder="Pat Johnson" name="signerName"
               value="${locals.dsConfig.signerName}" required />
    </div>
    <div class="form-group">
        <label for="signer2Email">Signer2 Email</label>
        <input type="email" class="form-control" id="signer2Email" name="signerEmail2"
               aria-describedby="emailHelp" placeholder="bob@mail.com" required />
        <small class="form-text text-muted">${launcherTexts.getHelpingTexts().getEmailWontBeShared()}</small>
    </div>
    <div class="form-group">
        <label for="signer2Name">Signer2 Name</label>
        <input type="text" class="form-control" id="signer2Name" placeholder="Bob" name="signerName2" required />
    </div>
    <div class="form-group">
        <label for="ccEmail">CC Email</label>
        <input type="email" class="form-control" id="ccEmail" name="ccEmail"
               aria-describedby="emailHelp" placeholder="pat@example.com" required />
        <small class="form-text text-muted">${launcherTexts.getHelpingTexts().getCCEmailShouldDifferFromSigner()}}</small>
    </div>
    <div class="form-group">
        <label for="ccName">CC Name</label>
        <input type="text" class="form-control" id="ccName" placeholder="Pat Johnson" name="ccName"
               required />
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

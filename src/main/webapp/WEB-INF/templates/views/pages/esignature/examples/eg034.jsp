<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="firstFormNumber" value="0" scope="page" />
<c:set var="secondFormNumber" value="1" scope="page" />
<c:set var="thirdFormNumber" value="2" scope="page" />
<c:set var="signerEmailInputNumber" value="0" scope="page" />
<c:set var="signerNameInputNumber" value="1" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test="${showDoc}">
    <p><a target="_blank" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>
<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="signerEmail">
            ${example.getForms().get(firstFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}
        </label>

        <input type="email"
               class="form-control"
               id="signerEmail"
               name="signerEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(firstFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
               required
               value="${locals.dsConfig.signerEmail}">

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>
    <div class="form-group">
        <label for="signerName">
            ${example.getForms().get(firstFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}
        </label>

        <input type="text"
               class="form-control"
               id="signerName"
               placeholder="${example.getForms().get(firstFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
               name="signerName"
               value="${locals.dsConfig.signerName}"
               required>
    </div>

    <div class="form-group">
        <label for="signerNotCheckedEmail">
            ${example.getForms().get(secondFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}
        </label>

        <input type="email"
               class="form-control"
               id="signerNotCheckedEmail"
               name="signerNotCheckedEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(secondFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
               required
               value="${locals.dsConfig.signerEmail}">

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>
    <div class="form-group">
        <label for="signerNotCheckedName">
            ${example.getForms().get(secondFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}
        </label>

        <input type="text"
               class="form-control"
               id="signerNotCheckedName"
               placeholder="${example.getForms().get(secondFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
               name="signerNotCheckedName"
               value="${locals.dsConfig.signerName}"
               required>
    </div>

    <div class="form-group">
        <label for="signerCheckedEmail">
            ${example.getForms().get(thirdFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}
        </label>

        <input type="email"
               class="form-control"
               id="signerCheckedEmail"
               name="signerCheckedEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(thirdFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
               required
               value="${locals.dsConfig.signerEmail}">

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>
    <div class="form-group">
        <label for="signerCheckedName">
            ${example.getForms().get(thirdFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}
        </label>

        <input type="text"
               class="form-control"
               id="signerCheckedName"
               placeholder="${example.getForms().get(thirdFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
               name="signerCheckedName"
               value="${locals.dsConfig.signerName}"
               required>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
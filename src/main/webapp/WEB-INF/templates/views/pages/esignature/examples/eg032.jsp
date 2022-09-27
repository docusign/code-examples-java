<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="firstFormNumber" value="0" scope="page" />
<c:set var="secondFormNumber" value="1" scope="page" />
<c:set var="nameNumber" value="0" scope="page" />
<c:set var="emailInputNumber" value="1" scope="page" />
<c:set var="email2InputNumber" value="0" scope="page" />
<c:set var="name2InputNumber" value="1" scope="page" />

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
    <p>${example.getForms().get(firstFormNumber).getFormName()}</p>
    <div class="form-group">
        <label for="signerEmail">
            ${example.getForms().get(firstFormNumber).getInputs().get(emailInputNumber).getInputPlaceholder()}
        </label>

        <input type="email"
               class="form-control"
               id="signerEmail"
               name="signerEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(firstFormNumber).getInputs().get(emailInputNumber).getInputPlaceholder()}"
               required
               value="${locals.dsConfig.signerEmail}">

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>
    <div class="form-group">
        <label for="signerName">
            ${example.getForms().get(firstFormNumber).getInputs().get(nameNumber).getInputPlaceholder()}
        </label>

        <input type="text"
               class="form-control"
               id="signerName"
               placeholder="${example.getForms().get(firstFormNumber).getInputs().get(nameNumber).getInputPlaceholder()}"
               name="signerName"
               value="${locals.dsConfig.signerName}"
               required>
    </div>

    <p>${example.getForms().get(secondFormNumber).getFormName()}</p>
    <div class="form-group">
        <label for="signerEmail2">
            ${example.getForms().get(secondFormNumber).getInputs().get(email2InputNumber).getInputPlaceholder()}
        </label>

        <input type="email"
               class="form-control"
               id="signerEmail2"
               name="signerEmail2"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(secondFormNumber).getInputs().get(email2InputNumber).getInputPlaceholder()}"
               required
               value="${locals.dsConfig.signerEmail}">

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>
    <div class="form-group">
        <label for="signerName2">
            ${example.getForms().get(secondFormNumber).getInputs().get(name2InputNumber).getInputPlaceholder()}
        </label>

        <input type="text"
               class="form-control"
               id="signerName2"
               placeholder="${example.getForms().get(secondFormNumber).getInputs().get(name2InputNumber).getInputPlaceholder()}"
               name="signerName2"
               value="${locals.dsConfig.signerName}"
               required>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
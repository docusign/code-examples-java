<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="envelopeIdInputNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="envelopeId">
            ${example.getForms().get(formNumber).getInputs().get(envelopeIdInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="envelopeId"
               name="envelopeId"
               aria-describedby="envelopeIdHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(envelopeIdInputNumber).getInputPlaceholder()}"
               required
               value="${envelopeId}">

        <small id="envelopeIdHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getDefaultEnvelopeId()}
        </small>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getHelpingTexts().getSubmitButtonDeleteText()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

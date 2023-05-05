<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="newAgentEmailInputNumber" value="0" scope="page" />
<c:set var="newAgentNameInputNumber" value="1" scope="page" />
<c:set var="activationCodeInputNumber" value="2" scope="page" />

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
        <label for="agentEmail">
            ${example.getForms().get(formNumber).getInputs().get(newAgentEmailInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="agentEmail"
               name="agentEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(newAgentEmailInputNumber).getInputPlaceholder()}"
               value="${locals.dsConfig.signerEmail}"
               required>
    </div>
    <div class="form-group">
        <label for="agentName">
            ${example.getForms().get(formNumber).getInputs().get(newAgentNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="agentName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(newAgentNameInputNumber).getInputPlaceholder()}"
               name="agentName"
               value="${locals.dsConfig.signerName}"
               required>
    </div>
    <div class="form-group">
        <label for="activationCode">
            ${example.getForms().get(formNumber).getInputs().get(activationCodeInputNumber).getInputName()}
        </label>

        <input type="tel"
               class="form-control"
               id="activationCode"
               name="activationCode"
               aria-describedby="SaveAgentActivationCode"
               placeholder="${example.getForms().get(formNumber).getInputs().get(activationCodeInputNumber).getInputPlaceholder()}"
               required/>


        <small id="SaveActivationCode" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getSaveAgentActivationCode()}
        </small>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

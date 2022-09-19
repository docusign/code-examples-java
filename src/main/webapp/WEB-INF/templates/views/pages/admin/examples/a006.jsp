<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="emailInputNumber" value="0" scope="page" />

<h4>6. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="email">
            ${example.getForms().get(formNumber).getInputs().get(emailInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="email"
               name="email"
               placeholder="${example.getForms().get(formNumber).getInputs().get(emailInputNumber).getInputPlaceholder()}"
               required>

        <small id="accessHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>

        <br/>
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
    </div>
</form>

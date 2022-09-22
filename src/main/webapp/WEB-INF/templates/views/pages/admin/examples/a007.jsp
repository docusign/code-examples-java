<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="userIdInputNumber" value="0" scope="page" />

<h4>7. ${example.getExampleName()}</h4>
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
    <label for="userId">
        ${example.getForms().get(formNumber).getInputs().get(userIdInputNumber).getInputName()}
    </label>

    <input type="text"
           class="form-control"
           id="userId"
           name="userId"
           placeholder="${example.getForms().get(formNumber).getInputs().get(userIdInputNumber).getInputPlaceholder()}"
           required
           pattern="[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}">

    <br/>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-primary">${launcherTexts.getContinueButton()}</button>
</div>
</form>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="roomNameInputNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="formGroupName">
            ${example.getForms().get(formNumber).getInputs().get(roomNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="formGroupName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(roomNameInputNumber).getInputPlaceholder()}"
               name="formGroupName"
               required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
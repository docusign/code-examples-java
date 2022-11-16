<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="clickwrapInputNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="clickwrapName">
            ${example.getForms().get(formNumber).getInputs().get(clickwrapInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="clickwrapName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(clickwrapInputNumber).getInputPlaceholder()}"
               name="clickwrapName"
               required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="folderNameInputNumber" value="1" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>${restoreText}</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="folderName">
            ${example.getForms().get(formNumber).getInputs().get(folderNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="folderName"
               name="folderName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(folderNameInputNumber).getInputPlaceholder()}"
               required
               value="${example.getForms().get(formNumber).getInputs().get(folderNameInputNumber).getInputPlaceholder()}">
   </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getHelpingTexts().getSubmitButtonRestoreText()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

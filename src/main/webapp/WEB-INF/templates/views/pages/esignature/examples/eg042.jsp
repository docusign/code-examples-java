<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="candidateEmailIndex" value="0" scope="page" />
<c:set var="candidateNameIndex" value="1" scope="page" />


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
    <p>
        ${example.getForms().get(formNumber).getFormName()}
    </p>
    <div class="form-group">
        <label for="candidateEmail">
            ${example.getForms().get(formNumber).getInputs().get(candidateEmailIndex).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="candidateEmail"
               name="candidateEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(candidateEmailIndex).getInputPlaceholder()}"
               required>

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>
    <div class="form-group">
      <label for="candidateName">
          ${example.getForms().get(formNumber).getInputs().get(candidateNameIndex).getInputName()}
      </label>

      <input type="text"
             class="form-control"
             id="candidateName"
             placeholder="${example.getForms().get(formNumber).getInputs().get(candidateNameIndex).getInputPlaceholder()}"
             name="candidateName"
             required>
    </div>
   

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

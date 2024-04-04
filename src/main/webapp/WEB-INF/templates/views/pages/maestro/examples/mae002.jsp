<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="workflowIdInputNumber" value="0" scope="page" />
<c:set var="instanceIdInputNumber" value="1" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='mae001'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<c:if test="${showDoc}">
  <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
  ${viewSourceFile}
</p>

<c:choose>
  <c:when test="${workflowId != null && instanceId != null}">
    <p>${example.getForms().get(formNumber).getFormName()}</p>

    <form class="eg" action="" method="post" data-busy="form">
      <div class="form-group">
        <label for="workflow_id">
            ${example.getForms().get(formNumber).getInputs().get(workflowIdInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="workflow_id"
               name="workflow_id"
               value="${workflowId}"
               readonly
               placeholder="${example.getForms().get(formNumber).getInputs().get(workflowIdInputNumber).getInputPlaceholder()}"
               required>
      </div>

      <div class="form-group">
        <label for="instance_id">
            ${example.getForms().get(formNumber).getInputs().get(instanceIdInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="instance_id"
               name="instance_id"
               value="${instanceId}"
               readonly
               placeholder="${example.getForms().get(formNumber).getInputs().get(instanceIdInputNumber).getInputPlaceholder()}"
               required>
      </div>

      <input type="hidden" name="_csrf" value="${csrfToken}">
      <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
    </form>
  </c:when>
  <c:otherwise>
    <p>
        ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
    </p>

    <form class="eg" action="mae001" method="get">
      <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
    </form>
  </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>

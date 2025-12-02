<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="assigneeEmailInputNumber" value="0" scope="page" />
<c:set var="noWorkspaceRedirect" value="0" scope="page" />
<c:set var="noCreatorIdRedirect" value="1" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='work001'" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${!workspaceOk}">
        <p>
            ${example.getRedirectsToOtherCodeExamples().get(noWorkspaceRedirect).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
        </p>
    </c:when>
    <c:when test="${!creatorOk}">
        <p>
            ${example.getRedirectsToOtherCodeExamples().get(noCreatorIdRedirect).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
        </p>
    </c:when>
    <c:otherwise>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="assigneeEmail">
                    ${example.getForms().get(formNumber).getInputs().get(assigneeEmailInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="assigneeEmail"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(assigneeEmailInputNumber).getInputPlaceholder()}"
                       name="assigneeEmail"
                       required>

               <small id="emailHelp" class="form-text text-muted">
                   ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
               </small>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
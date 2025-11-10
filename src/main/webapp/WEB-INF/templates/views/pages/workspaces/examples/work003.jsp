<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="signerEmailInputNumber" value="0" scope="page" />
<c:set var="signerNameInputNumber" value="1" scope="page" />
<c:set var="redirectToFirstCodeExampleNumber" value="0" scope="page" />
<c:set var="redirectToSecondCodeExampleNumber" value="1" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='work001'" scope="page" />
<c:set var="redirectToSecondCodeExample" value="href='work002'" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${!workspaceOk}">
        <p>
            ${example.getRedirectsToOtherCodeExamples().get(redirectToFirstCodeExampleNumber).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
        </p>
    </c:when>
    <c:when test="${!documentOk}">
        <p>
            ${example.getRedirectsToOtherCodeExamples().get(redirectToSecondCodeExampleNumber).getRedirectText().replaceFirst("\\{0}", redirectToSecondCodeExample)}
        </p>
    </c:when>
    <c:otherwise>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="signerEmail">
                    ${example.getForms().get(formNumber).getInputs().get(signerEmailInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="signerEmail"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
                       name="signerEmail"
                       required>
            </div>
            <div class="form-group">
                <label for="signerName">
                    ${example.getForms().get(formNumber).getInputs().get(signerNameInputNumber).getInputName()}
                </label>
            
                <input type="text"
                       accept=".pdf"
                       class="form-control"
                       id="signerName"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
                       name="signerName"
                       required>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
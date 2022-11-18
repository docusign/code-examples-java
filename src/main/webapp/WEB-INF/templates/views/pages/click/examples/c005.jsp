<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="clickwrapInputNumber" value="0" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='c001'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${clickwraps['clickwraps'] != null }">
        <p>${example.getForms().get(formNumber).getFormName()}</p>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="clickwrapId">
                        ${example.getForms().get(formNumber).getInputs().get(clickwrapInputNumber).getInputName()}
                </label>

                <select class="custom-select" id="clickwrapId" name="clickwrapId">
                    <c:forEach items="${clickwraps['clickwraps']}" var="clickwrap">
                        <option value="${clickwrap['clickwrapId']}">${clickwrap['clickwrapName']}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
                ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
        </p>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>
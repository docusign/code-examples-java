<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="clickwrapInputNumber" value="0" scope="page" />
<c:set var="fullNameInputNumber" value="1" scope="page" />
<c:set var="emailAddressInputNumber" value="2" scope="page" />
<c:set var="companyInputNumber" value="3" scope="page" />
<c:set var="jobTitleInputNumber" value="4" scope="page" />
<c:set var="dateInputNumber" value="5" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='c001'" scope="page" />
<c:set var="redirectToSecondCodeExample" value="href='c002'" scope="page" />
<c:set var="redirectNumberTo1" value="0" scope="page" />
<c:set var="redirectNumberTo2" value="1" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${clickwraps['clickwraps'].size() > 0 }">
        <p>${example.getForms().get(formNumber).getFormName()}</p>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="clickwrap">
                        ${example.getForms().get(formNumber).getInputs().get(clickwrapInputNumber).getInputName()}
                </label>

                <select class="custom-select" id="clickwrap" name="clickwrap">
                    <c:forEach items="${clickwraps['clickwraps']}" var="clickwrap">
                        <option value="${clickwrap['clickwrapId']}">${clickwrap['clickwrapName']}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="fullName">
                        ${example.getForms().get(formNumber).getInputs().get(fullNameInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="fullName"
                       name="fullName"
                       required>
            </div>
            <div class="form-group">
                <label for="email">
                        ${example.getForms().get(formNumber).getInputs().get(emailAddressInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="email"
                       name="email"
                       required>
            </div>
            <div class="form-group">
                <label for="company">
                        ${example.getForms().get(formNumber).getInputs().get(companyInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="company"
                       name="company"
                       required>
            </div>
            <div class="form-group">
                <label for="title">
                        ${example.getForms().get(formNumber).getInputs().get(jobTitleInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="title"
                       name="title"
                       required>
            </div>
            <div class="form-group">
                <label for="date">
                        ${example.getForms().get(formNumber).getInputs().get(dateInputNumber).getInputName()}
                </label>

                <input type="date"
                       class="form-control"
                       id="date"
                       name="date"
                       required>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${hasInactiveClickraps}">
                <p>
                    ${example.getRedirectsToOtherCodeExamples().get(redirectNumberTo2).getRedirectText().replaceFirst("\\{0}", redirectToSecondCodeExample)}
                </p>
            </c:when>
            <c:otherwise>
                <p>
                    ${example.getRedirectsToOtherCodeExamples().get(redirectNumberTo1).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
                </p>
            </c:otherwise>
        </c:choose>
   </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
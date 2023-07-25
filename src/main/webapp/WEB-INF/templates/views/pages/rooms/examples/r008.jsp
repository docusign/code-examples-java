<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="formInputNumber" value="0" scope="page" />
<c:set var="officeInputNumber" value="1" scope="page" />
<c:set var="redirectToSeventhCodeExample" value="href='r007'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />


<form class="eg" action="" method="post" data-busy="form">
    <c:choose>
        <c:when test="${formGroupList == null || formGroupList.size() == 0}">
            ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToSeventhCodeExample)}
        </c:when>
        <c:when test="${officeList == null || officeList.size() == 0}">
            <p>Problem: cannot find any offices</p>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <label for="formGroupId">
                    ${example.getForms().get(formNumber).getInputs().get(formInputNumber).getInputName()}
                </label>
                <select id="formGroupId" name="formGroupId" class="form-control">
                    <c:forEach items="${formGroupList}" var="formGroup">
                        <option value="${formGroup.formGroupId}" selected>${formGroup.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="officeId">
                    ${example.getForms().get(formNumber).getInputs().get(officeInputNumber).getInputName()}
                </label>
                <select id="officeId" name="officeId" class="form-control">
                    <c:forEach items="${officeList}" var="office">
                        <option value="${office.officeId}" selected>${office.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </c:otherwise>
    </c:choose>
</form>


<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="brandNameInputNumber" value="0" scope="page" />
<c:set var="languageInputNumber" value="1" scope="page" />

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
        <label for="brandName">
            ${example.getForms().get(formNumber).getInputs().get(brandNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="brandName"
               name="brandName"
               aria-describedby="info"
               placeholder="${example.getForms().get(formNumber).getInputs().get(brandNameInputNumber).getInputPlaceholder()}"
               required>
    </div>
    <div class="form-group">
        <label for="language">
            ${example.getForms().get(formNumber).getInputs().get(languageInputNumber).getInputName()}
        </label>

        <select id="language" name="language" class="form-control">
            <c:forEach items="${listLanguage}" var="language">
                <option value="${language.code}">${language.name}</option>
            </c:forEach>
        </select>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="startDateInputNumber" value="0" scope="page" />
<c:set var="endDateInputNumber" value="1" scope="page" />

<h4>2. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<p>
    ${example.getForms().get(formNumber).getFormName()}
</p>

<form class="eg" action="" method="post" data-busy="form">

    <div class="form-group">
        <label for="startDate">
            ${example.getForms().get(formNumber).getInputs().get(startDateInputNumber).getInputName()}
        </label>

        <input type="date"
               name="startDate"
               id="startDate"
               value="${startDate}"
               class="form-control"
               placeholder="yyyy-MM-dd">
    </div>
    <div class="form-group">
        <label for="endDate">
            ${example.getForms().get(formNumber).getInputs().get(endDateInputNumber).getInputName()}
        </label>

        <input type="date"
               name="endDate"
               id="endDate"
               value="${endDate}"
               class="form-control"
               placeholder="yyyy-MM-dd">
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
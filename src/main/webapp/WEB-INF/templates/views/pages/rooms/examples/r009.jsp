<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>9. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />


<form class="eg" action="" method="post" data-busy="form">
    <c:choose>
        <c:when test="${formGroupList == null || formGroupList.size() == 0}">
            <p>Problem: please first create a form group using <a href="r007">example 7</a> </p>
        </c:when>
        <c:when test="${formList == null || formList.size() == 0}">
            <p>Problem: cannot find any forms, please first create a form </p>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <label for="formGroupId">Form Group</label>
                <select id="formGroupId" name="formGroupId" class="form-control">
                    <c:forEach items="${formGroupList}" var="formGroup">
                        <option value="${formGroup.formGroupId}" selected>${formGroup.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="formId">Form</label>
                <select id="formId" name="formId" class="form-control">
                    <c:forEach items="${formList}" var="form">
                        <option value="${form.libraryFormId}" selected>${form.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </c:otherwise>
    </c:choose>
</form>


<jsp:include page="../../../partials/foot.jsp"/>
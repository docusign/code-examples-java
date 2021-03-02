<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>8. Granting office access to a form group.</h4>

<p>This example demonstrates how to assign an office to
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/rooms101/forms/managing-forms-rooms/">a form group</a>
    for your DocuSign Rooms for Real Estate account. Granting office access to a form group will
    enable you to filter which form groups are available based on that office.
</p>

<p>API method used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/grantofficeaccesstoformgroup">FormGroups:GrantOfficeAccessToFormGroup</a>,
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/getformgroups">FormGroups:GetFormGroups</a> and
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/offices/offices/getoffices">Offices:GetOffices</a>.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <c:choose>
        <c:when test="${formGroupList == null || formGroupList.size() == 0}">
            <p>Problem: please first create a form group using <a href="r007">example 7</a> </p>
        </c:when>
        <c:when test="${officeList == null || officeList.size() == 0}">
            <p>Problem: cannot find any offices</p>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <label for="formGroupId">Office</label>
                <select id="formGroupId" name="formGroupId" class="form-control">
                    <c:forEach items="${formGroupList}" var="formGroup">
                        <option value="${formGroup.formGroupId}" selected>${formGroup.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="officeId">Office</label>
                <select id="officeId" name="officeId" class="form-control">
                    <c:forEach items="${officeList}" var="office">
                        <option value="${office.officeId}" selected>${office.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </c:otherwise>
    </c:choose>
</form>


<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="permissionInputNumber" value="0" scope="page" />
<c:set var="groupInputNumber" value="1" scope="page" />

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
    <div class="form-group">
        <label for="profileId">
            ${example.getForms().get(formNumber).getInputs().get(permissionInputNumber).getInputName()}
        </label>

        <select id="profileId" name="profileId" class="form-control">
            <c:forEach items="${listProfiles}" var="profile">
                <option value="${profile.permissionProfileId}">${profile.permissionProfileName}</option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="groupId">
            ${example.getForms().get(formNumber).getInputs().get(groupInputNumber).getInputName()}
        </label>

        <select id="groupId" name="groupId" class="form-control">
            <c:forEach items="${listGroups}" var="group">
                <option value="${group.groupId}">${group.groupName}</option>
            </c:forEach>
        </select>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
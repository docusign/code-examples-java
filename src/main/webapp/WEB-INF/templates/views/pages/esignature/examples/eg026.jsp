<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="profileNameInputNumber" value="0" scope="page" />

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
            ${example.getForms().get(formNumber).getInputs().get(profileNameInputNumber).getInputName()}
        </label>

        <select id="profileId" name="profileId" class="form-control" onchange="GetSelectedTextValue(this)">
            <c:forEach items="${listProfiles}" var="profile">
                <option value="${profile.permissionProfileId}" 
                    ${profile.permissionProfileId == profileId ? 'selected="selected"' : ''}>
                ${profile.permissionProfileName}</option>
            </c:forEach>
        </select>
    </div>

    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<script type="text/javascript">
    function GetSelectedTextValue(listProfiles) {
        var selectedValue = listProfiles.value;
        const http = new XMLHttpRequest();
        http.open("GET",'eg026/profile?profileId=' + selectedValue, false);
        http.send();

    }
</script>

<jsp:include page="../../../partials/foot.jsp"/>
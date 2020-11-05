<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>26. ${title}.</h4>

<p>
This code example demonstrates how to edit individual permission settings on a permissions profile 
with the eSignature REST API.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountPermissionProfiles/update">AccountPermissionProfiles::update</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="profileId">Select permission profile to update</label>
        <select id="profileId" name="profileId" class="form-control" onchange="GetSelectedTextValue(this)">
            <c:forEach items="${listProfiles}" var="profile">
                <option value="${profile.permissionProfileId}" 
                    ${profile.permissionProfileId == profileId ? 'selected="selected"' : ''}>
                ${profile.permissionProfileName}</option>
            </c:forEach>
        </select>
    </div>

<%--    <div class="form-group">--%>
<%--        <label for="permissionProfileName">Profile Name (change it)</label>--%>
<%--        <input type="text" class="form-control" id="permissionProfileName" name="permissionProfileName" value="${permissionProfileName}" required>--%>
<%--    </div>--%>
<%--    <c:if test="${empty permissions}">--%>
<%--        <p>There is no account role settings is defined</p>--%>
<%--    </c:if>--%>
<%--    <table id="permisions">--%>
<%--        <caption></caption>--%>
<%--        <c:forEach items="${permisions}" var="property">--%>
<%--            <tr>--%>
<%--                <td>${property.name}</td>--%>
<%--                <td><strong>${property.leftValue}</strong></td>--%>
<%--            </tr>--%>
<%--        </c:forEach>--%>
<%--    </table>--%>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
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
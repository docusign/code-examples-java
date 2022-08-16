<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>


<h4>8. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
   View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${emailAddress != null }">
        <p>Update user product permission profile for the following email: <b>${emailAddress}</b></p>

        <form class="eg" method="post" data-busy="form">
            <div class="form-group">
                <label for="Products">Choose a product for which you want to update the permission profile</label>
                <select id="Products" name="ProductId" class="form-control">
                    <c:forEach var="entry" items="${listProducts}">
                        <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="PermissionProfilesFiltered">Choose a permission profile</label>
                <select id="PermissionProfilesFiltered" name="PermissionProfileId" class="form-control">
                    <c:forEach items="${listPermissionProfiles.permissionProfiles}" var="profile">
                        <option value="${profile.permissionProfileId}">${profile.permissionProfileName}</option>
                    </c:forEach>
                </select>
            </div>

            <input type="hidden" name="csrf_token" value="${csrfToken}"/>
            <button type="submit" class="btn btn-primary">Continue</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            Problem: You do not have the user to change permissions for. Go to example#2 and create one:
            <a href="a002">create active CLM/eSign User.</a> <br/>
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page='../../../partials/foot.jsp'/>

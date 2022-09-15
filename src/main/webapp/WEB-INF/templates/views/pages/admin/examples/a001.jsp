<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>1. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="userName">User Name</label>
        <input type="text" class="form-control" id="userName" placeholder="User name" name="userName" required>
    </div>

    <div class="form-group">
        <label for="firstName">First Name</label>
        <input type="text" class="form-control" id="firstName" placeholder="First name" name="firstName" required>
    </div>

    <div class="form-group">
        <label for="lastName">Last Name</label>
        <input type="text" class="form-control" id="lastName" placeholder="Last name" name="lastName" required>
    </div>
    <div class="form-group">
        <label for="email">Signer Email</label>
        <input type="email" class="form-control" id="email" name="email"
               aria-describedby="emailHelp" placeholder="some_email@example.com" required>
        <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
    </div>

    <div class="form-group">
        <label for="profileId">eSignature permission profile</label>
        <select id="profileId" name="profileId" class="form-control">
            <c:forEach items="${listProfiles}" var="profile">
                <option value="${profile.permissionProfileId}">${profile.permissionProfileName}</option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="groupId">eSignature group</label>
        <select id="groupId" name="groupId" class="form-control">
            <c:forEach items="${listGroups}" var="group">
                <option value="${group.groupId}">${group.groupName}</option>
            </c:forEach>
        </select>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page='../../../partials/foot.jsp'/>

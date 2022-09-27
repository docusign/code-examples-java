<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="userNameInputNumber" value="0" scope="page" />
<c:set var="firstNameInputNumber" value="1" scope="page" />
<c:set var="secondNameInputNumber" value="2" scope="page" />
<c:set var="emailInputNumber" value="3" scope="page" />
<c:set var="eSignPermissionInputNumber" value="4" scope="page" />
<c:set var="groupInputNumber" value="5" scope="page" />

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
        <label for="userName">
            ${example.getForms().get(formNumber).getInputs().get(userNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="userName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(userNameInputNumber).getInputPlaceholder()}"
               name="userName"
               required>
    </div>

    <div class="form-group">
        <label for="firstName">
            ${example.getForms().get(formNumber).getInputs().get(firstNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="firstName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(firstNameInputNumber).getInputPlaceholder()}"
               name="firstName"
               required>
    </div>

    <div class="form-group">
        <label for="lastName">
            ${example.getForms().get(formNumber).getInputs().get(secondNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="lastName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(secondNameInputNumber).getInputPlaceholder()}"
               name="lastName"
               required>
    </div>
    <div class="form-group">
        <label for="email">
            ${example.getForms().get(formNumber).getInputs().get(emailInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="email"
               name="email"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(emailInputNumber).getInputPlaceholder()}"
               required>

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
        </small>
    </div>

    <div class="form-group">
        <label for="profileId">
            ${example.getForms().get(formNumber).getInputs().get(eSignPermissionInputNumber).getInputName()}
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

<jsp:include page='../../../partials/foot.jsp'/>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp' />

<c:set var="formNumber" value="0" scope="page" />
<c:set var="userNameInputNumber" value="0" scope="page" />
<c:set var="firstNameInputNumber" value="1" scope="page" />
<c:set var="secondNameInputNumber" value="2" scope="page" />
<c:set var="emailInputNumber" value="3" scope="page" />
<c:set var="eSignPermissionInputNumber" value="4" scope="page" />
<c:set var="clmPermissionInputNumber" value="5" scope="page" />
<c:set var="groupInputNumber" value="6" scope="page" />

    <h4>2. ${example.getExampleName()}</h4>
    <p>${example.getExampleDescription()}</p>

    <c:if test='${showDoc}'>
        <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.
        </p>
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
            <label for="eSignProfileId">
                ${example.getForms().get(formNumber).getInputs().get(eSignPermissionInputNumber).getInputName()}
            </label>

            <select id="eSignProfileId" name="eSignProfileId" class="form-control">
                <c:forEach items="${listeSign.permissionProfiles}" var="esign">
                    <option value="${esign.permissionProfileId}">${esign.permissionProfileName}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="clmProfileId">
                ${example.getForms().get(formNumber).getInputs().get(clmPermissionInputNumber).getInputName()}
            </label>

            <select id="clmProfileId" name="clmProfileId" class="form-control">
                <c:forEach items="${listCLM.permissionProfiles}" var="profile">
                    <option value="${profile.permissionProfileId}">${profile.permissionProfileName}</option>
                </c:forEach>
            </select>
        </div>


        <div class="form-group">
            <label for="dsGroupId">
                ${example.getForms().get(formNumber).getInputs().get(groupInputNumber).getInputName()}
            </label>

            <select id="dsGroupId" name="dsGroupId" class="form-control">
                <c:forEach items="${listGroups.dsGroups}" var="group">
                    <option value="${group.dsGroupId}">${group.groupName}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>

        <input type="hidden" name="_csrf" value="${csrfToken}">
        <div class="form-group">
            <label for="clmProductId"></label>
            <input type="hidden" class="form-control" id="clmProductId" name="clmProductId" value="${clmProductId}">
        </div>
        <div class="form-group">
            <label for="esignProductId"></label>
            <input type="hidden" class="form-control" id="eSignProductId" name="eSignProductId"
                value="${eSignProductId}">
        </div>
    </form>
    <br />
    <br />
    <jsp:include page='../../../partials/foot.jsp' />
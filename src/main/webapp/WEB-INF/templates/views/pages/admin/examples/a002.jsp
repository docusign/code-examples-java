<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
    <jsp:include page='../../../partials/head.jsp' />

    <h4>2. ${example.getExampleName()}</h4>
    <p>${example.getExampleDescriptionExtended()}</p>

    <c:if test='${showDoc}'>
        <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.
        </p>
    </c:if>


    <jsp:include page="../../links_to_api_methods.jsp" />

    <p>
        View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
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
            <input type="email" class="form-control" id="email" name="email" aria-describedby="emailHelp"
                placeholder="some_email@example.com" required>
            <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
        </div>

        <div class="form-group">
            <label for="eSignProfileId">eSignature Permission Profile</label>
            <select id="eSignProfileId" name="eSignProfileId" class="form-control">
                <c:forEach items="${listeSign.permissionProfiles}" var="esign">
                    <option value="${esign.permissionProfileId}">${esign.permissionProfileName}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="clmProfileId">CLM Permission Profile</label>
            <select id="clmProfileId" name="clmProfileId" class="form-control">
                <c:forEach items="${listCLM.permissionProfiles}" var="profile">
                    <option value="${profile.permissionProfileId}">${profile.permissionProfileName}</option>
                </c:forEach>
            </select>
        </div>


        <div class="form-group">
            <label for="dsGroupId">DocuSign Admin Group</label>
            <select id="dsGroupId" name="dsGroupId" class="form-control">
                <c:forEach items="${listGroups.dsGroups}" var="group">
                    <option value="${group.dsGroupId}">${group.groupName}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-docu">Submit</button>

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
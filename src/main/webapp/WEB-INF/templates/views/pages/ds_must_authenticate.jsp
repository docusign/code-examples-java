<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp" />

<div style="margin:1% 5%;">
    ${launcherTexts.getLoginPage().getLoginHeader()}
    <form class="eg" action="/ds/authenticate" method="post" data-busy="form">
        <div class="form-group">
        <select name="selectAuthType">
            <c:forEach var="item" items="${authTypes}">
                <option value="${item.key}">${item.value}</option>
            </c:forEach>
        </select>
    </div>
        <p class="lead" style="padding-top: .5rem;">
            <button type="submit" class="btn btn-docu">${launcherTexts.getLoginPage().getLoginButton()}</button>
        </p>

    </form>
    <hr class="my-4">
    <p>${launcherTexts.getLoginPage().getLoginHelperText()}</p>
</div>
<jsp:include page="../partials/foot.jsp" />

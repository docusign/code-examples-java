<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp" />

<div style="margin:1% 5%;">
    <h1 class="display-4">Please Authenticate with DocuSign</h1>
    <p>Please choose your authentication type</p>
    <form class="eg" action="/ds/authenticate" method="post" data-busy="form">
        <div class="form-group">
        <select name="selectAuthType">
            <c:forEach var="item" items="${authTypes}">
                <option value="${item.key}">${item.value}</option>
            </c:forEach>
        </select>
    </div>
        <p class="lead" style="padding-top: .5rem;">
            <button type="submit" class="btn btn-docu">Authenticate with DocuSign</button>
        </p>

    </form>
    <hr class="my-4">
    <p>You need to authenticate with DocuSign to continue your request.</p>
</div>
<jsp:include page="../partials/foot.jsp" />

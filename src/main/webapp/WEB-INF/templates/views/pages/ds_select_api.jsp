<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp" />

<div style="margin:1% 5%;">
    <h1 class="display-4">Please choose an API</h1>
    <p>Please choose your API type</p>
    <form class="eg" action="/ds/selectApi" method="post" data-busy="form">
        <div class="form-group">
            <select name="selectApiType">
                <c:forEach var="item" items="${apiTypes}">
                    <option value="${item.key}">${item.value}</option>
                </c:forEach>
            </select>
        </div>
        <p class="lead" style="padding-top: .5rem;">
            <button type="submit" class="btn btn-docu">Choose your desired API</button>
        </p>

    </form>
    <hr class="my-4">
</div>

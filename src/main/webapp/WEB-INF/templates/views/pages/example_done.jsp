<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<h2>${done.name}</h2>
<p>${done.message}</p>

<c:if test="${not empty done.json}">
    <!-- Data from the server -->
    <div id="server_json_data" data-server-json-data='{"json": ${done.json}}' class="hidden"></div>

    <c:set var="formattedJson" value="${done.json.trim()}" />

    <c:choose>
        <c:when test="${formattedJson.contains('url')}">
            <p>
                <code>${formattedJson}</code>
            </p>
        </c:when>
        <c:otherwise>
            <p>
                <pre class="json-display"><code id="json-display">${done.json}</code></pre>
            </p>
        </c:otherwise>
    </c:choose>
</c:if>



<!-- Add the c:if condition for formFillUrl -->
<c:if test="${not empty done.formFillUrl}">
    <!--  //ds-snippet-start:Rooms6Step5 -->
    <iframe src="${done.formFillUrl}" width="100%" height="500" frameborder="0"></iframe>
    <!-- //ds-snippet-end:Rooms6Step5 -->
</c:if>


<c:if test="${not empty locals.session.importId}">
    <!-- Check request status if a batch ID is detected Admin ex04 -->
    <form action="a004a" method="POST">
        <input type="submit" class="btn btn-link" style="padding:0" value="Check the request status">
    </form>
</c:if>

<c:choose>
    <c:when test="${not empty done.redirect}">
        <p><a href="${done.redirect}">${launcherTexts.getContinueButton()}</a></p>
    </c:when>
    <c:otherwise>
        <p><a href="/">${launcherTexts.getContinueButton()}</a></p>
    </c:otherwise>
</c:choose>

<jsp:include page="../partials/foot.jsp"/>

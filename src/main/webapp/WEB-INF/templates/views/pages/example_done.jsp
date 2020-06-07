<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<h2>${done.name}</h2>
<p>${done.message}</p>

<c:if test="${not empty done.json}">
    <!-- Data from the server -->
    <div id="server_json_data" data-server-json-data='{"json": ${done.json}}' class="hidden"></div>
    <p>
        <pre class="json-display"><code id="json-display">${done.json}</code></pre>
    </p>
</c:if>

<p><a href="/">Continue</a></p>

<jsp:include page="../partials/foot.jsp"/>

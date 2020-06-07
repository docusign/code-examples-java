<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<h2>${h1}</h2>
<p>${message}</p>

<c:if test="${not empty json}">
    <!-- Data from the server -->
    <div id="server_json_data" data-server-json-data='{"json": ${json}}' class="hidden"></div>
    <p>
    <pre class="json-display"><code id="json-display">${json}</code></pre>
    </p>
</c:if>

<p><a href="/">Continue</a></p>

<jsp:include page="../partials/foot.jsp"/>

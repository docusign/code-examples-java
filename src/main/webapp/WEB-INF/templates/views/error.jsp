<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="./partials/head.jsp"/>

<h2>Problem: an error occurred</h2>
<p>Error information:</p>

<c:choose>
    <c:when test="${not empty status}">
        <p>${status}: ${message}
        </p>
    </c:when>
    <c:otherwise>
        <p>
        <pre>${error}</pre>
        </p>
        <h3>Stack trace</h3>
        <p>
        <pre>${trace}</pre>
        </p>
    </c:otherwise>
</c:choose>


<p><a href="/">Continue</a></p>

<jsp:include page="./partials/foot.jsp"/>

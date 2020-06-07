<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="./partials/head.jsp"/>

<h2>${done.title}. ${done.name}</h2>
<p>The example <em>"${done.name}"</em> is failed. Error information:</p>
<p><strong>Message:</strong><br />${done.message}</p>
<p><strong>Stack trace:</strong><br /></p>
<ul id="stacktrace" style="list-style-type:none;font-size:smaller;">
    <c:forEach items="${done.stackTrace}" var="item">
        <li>${item.className}.${item.methodName}(${item.fileName}:${item.lineNumber})</li>
    </c:forEach>
</ul>

<p><a href="/">Continue</a></p>

<jsp:include page="./partials/foot.jsp"/>

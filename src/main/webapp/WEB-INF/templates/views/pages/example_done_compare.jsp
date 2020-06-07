<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<h2>${done.name}</h2>
<p>${done.message}</p>

<c:if test="${not empty done.diff}">
    <h5>Changed fields</h5>
    <table id="diff">
        <caption></caption>
        <tr>
            <th id="name">Property name</th>
            <th id="leftValue">Old value</th>
            <th id="rightValue">New value</th>
        </tr>
        <c:forEach items="${done.diff}" var="item">
            <tr>
                <td>${item.name}</td>
                <td>${item.leftValue}</td>
                <td>${item.rightValue}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<p><a href="/">Continue</a></p>

<jsp:include page="../partials/foot.jsp"/>

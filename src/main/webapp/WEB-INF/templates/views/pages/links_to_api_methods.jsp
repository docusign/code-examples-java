<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>
  <c:choose>
    <c:when test="${example.getLinksToAPIMethod().size() == 1}">
      <span>API method used:</span>
    </c:when>
    <c:otherwise>
      <span>API methods used:</span>
    </c:otherwise>
  </c:choose>

  <c:forEach var="link" items="${example.getLinksToAPIMethod()}">
    <a href="${link.getPath()}">
        ${link.getPathName()}
    </a>

    <c:choose>
      <c:when test="${example.getLinksToAPIMethod().size() == example.getLinksToAPIMethod().indexOf(link) + 1}">
        <span>.</span>
      </c:when>
      <c:when test="${example.getLinksToAPIMethod().size() - 1 == example.getLinksToAPIMethod().indexOf(link) + 1}">
        <span>and</span>
      </c:when>
      <c:otherwise>
        <span>,</span>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</p>

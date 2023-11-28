<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>
<c:if test="${example.getLinksToAPIMethod().size() != 0}">
  <c:choose>
    <c:when test="${example.getLinksToAPIMethod().size() == 1}">
      <span>${launcherTexts.getAPIMethodUsed()}</span>
    </c:when>
    <c:otherwise>
      <span>${launcherTexts.getAPIMethodUsedPlural()}</span>
    </c:otherwise>
  </c:choose>

  <c:forEach var="link" items="${example.getLinksToAPIMethod()}">
    <a href="${link.getPath()}">${link.getPathName()}</a>

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
</c:if>
</p>

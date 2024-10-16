<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<c:choose>
    <c:when test="${templateId == null}">
        <p>${example.getExampleDescription()}</p>
        <p>
            API methods used:
            <a target='_blank' href="https://developers.docusign.com/docs/esign-rest-api/reference/templates/templates/list/">
                Templates::list
            </a>, and
            <a target='_blank' href="https://developers.docusign.com/docs/esign-rest-api/reference/templates/templates/create/">
                Templates::create
            </a>.
        </p>
        <p>
            ${viewSourceFile}
        </p>
        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:when test="${templateId != null}">
        <c:forEach var="page" items="${example.getAdditionalPage()}">
            <c:if test="${page.getName() == 'create_web_form'}">
                <p>${page.getResultsPageText().replaceFirst("\\{0}", "src/main/resources")}</p>
            </c:if>
        </c:forEach>
        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:when>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>4a. ${example.getExampleName()}</h4>
<p>${example.getAdditionalPage().get(0).getResultsPageText()}</p>
<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${importOk == true}">
        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            Problem: please first bulk import user data <a target="_blank" href="/a004">example 4</a>.<br />
            Thank you.
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page='../../../partials/foot.jsp'/>

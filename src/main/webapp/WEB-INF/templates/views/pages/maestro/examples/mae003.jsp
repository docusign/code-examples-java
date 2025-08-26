<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>

<c:choose>
    <c:when test="${isWorkflowIdPresent == true}">
        <p>${example.getExampleDescription()}</p>
        <c:if test="${showDoc}">
            <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
        </c:if>

        <jsp:include page="../../links_to_api_methods.jsp" />

        <p>
                ${viewSourceFile}
        </p>

                <form class="eg" action="" method="post" data-busy="form">
                    <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
                </form>
    </c:when>
    <c:otherwise>
        <p>
                ${example.getCustomErrorTexts().get(0).getErrorMessage()}
        </p>
        <form class="eg" action="mae001" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>
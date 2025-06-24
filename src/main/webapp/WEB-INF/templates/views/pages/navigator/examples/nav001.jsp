<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:include page="../../../partials/head.jsp" />

    <c:set var="formNumber" value="0" scope="page" />
    <c:set var="signerEmailInputNumber" value="0" scope="page" />
    <c:set var="signerNameInputNumber" value="1" scope="page" />

    <h4>${example.getExampleName()}</h4>
    <p>${example.getExampleDescription()}</p>
    <c:if test="${showDoc}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
    </c:if>

    <jsp:include page="../../links_to_api_methods.jsp" />

    <p>
        ${viewSourceFile}
    </p>

    <form class="eg" action="" method="post" data-busy="form">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
    </form>

    <jsp:include page="../../../partials/foot.jsp" />
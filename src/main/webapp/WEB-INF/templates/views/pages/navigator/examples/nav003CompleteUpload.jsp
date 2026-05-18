<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:include page="../../../partials/head.jsp" />

    <c:set var="formNumber" value="0" scope="page" />

    <h4>${example.getExampleName()}</h4>
    <p>${example.getAdditionalPage().get(0).getResultsPageText()}</p>

    <form class="eg" action="" method="post" data-busy="form">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
    </form>

    <jsp:include page="../../../partials/foot.jsp" />

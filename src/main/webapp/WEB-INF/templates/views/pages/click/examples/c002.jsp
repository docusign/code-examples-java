<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>2. Activating a clickwrap.</h4>

<p>This topic describes how to use <a target ="_blank" rel="noopener noreferrer" href="https://www.docusign.com/products/click">DocuSign Click</a>
    to activate a new clickwrap that you have already created.
    By default, new clickwraps are inactive. You must activate your clickwrap before you can use it.
</p>
<p>API methods used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/updateclickwrapversion">ClickWraps:updateClickwrapVersion</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${clickwrapOk == true}">
        <p>The clickwrap you created via example 1 will be activated.</p>
        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Cannot find any clickwrap. Please first create a clickwrap using
            <a target="_blank" href="/c001">example 1</a>.
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>3. Create a new clickwrap version.</h4>

<p>
    Creates a new version of a clickwrap.
</p>
<p>
    You can specify whether you require users who have previously accepted the clickwrap
    to accept the new version when they return to your website.
</p>
<p>API methods used:
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/updateclickwrapversion">ClickWraps:createClickwrapVersion</a>
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${clickwrapOk == true}">
        <p>The clickwrap you created via example 1 will be queried.</p>
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
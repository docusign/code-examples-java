<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>5. Get clickwrap responses.</h4>

<p>
    Gets user responses to your clickwrap agreements.
</p>
<p>API methods used:
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/getclickwrapagreements">ClickWraps:getClickwrapAgreements</a>
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>
<p>
    The clickwrap you created via example 1 will be queried.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>


<jsp:include page="../../../partials/foot.jsp"/>
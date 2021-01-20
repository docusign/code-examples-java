<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>6. Get a list of clickwraps.</h4>

<p>This topic demonstrates how to use the Click API to get a list of clickwraps
    associated with a specific DocuSign user.
</p>
<p>API methods used:
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/getclickwraps">ClickWraps:getClickwraps</a>
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>


<jsp:include page="../../../partials/foot.jsp"/>
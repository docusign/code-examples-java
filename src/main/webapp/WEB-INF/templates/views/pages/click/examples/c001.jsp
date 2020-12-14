<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>1. Creating a clickwrap.</h4>

<p>This topic describes how to use <a target ="_blank" rel="noopener noreferrer" href="https://www.docusign.com/products/click">DocuSign Click</a>
    to create a clickwrap that you can embed in your website or app.<br>
    To learn how to generate a clickwrap automatically by using the UI, see the <a target ='_blank' rel="noopener noreferrer" href="https://support.docusign.com/en/guides/click-user-guide">Click User Guide</a>.
</p>
<p>API methods used:
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/createclickwrap">ClickWraps:createClickwrap</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="clickwrapName">Clickwrap Name</label>
        <input type="text" class="form-control" id="clickwrapName" placeholder="Please write clickwrap name" name="clickwrapName" required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
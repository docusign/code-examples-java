<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>4. Embeding a clickwrap.</h4>

<p>This topic describes how to use <a target ="_blank" rel="noopener noreferrer" href="https://www.docusign.com/products/click">DocuSign Click</a>
    to embed an existing clickwrap in your website. It walks you through the process of
    creating the JavaScript code you use to embed the clickwrap.

    You perform this step after you follow the steps in
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/how-to/create-clickwraps">How to create a clickwrap</a> and
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/how-to/activate-clickwrap">How to activate a clickwrap</a>.
    To learn how to automatically generate a clickwrap and its embed code by using the UI,
    see the <a target ="_blank" rel="noopener noreferrer" href="https://support.docusign.com/en/guides/click-user-guide">Click User Guide</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${clickwrapOk == true}">
        <p>The clickwrap you created via example 1 will be embedded.</p>
        <div id="ds-click"
             data-account-id="${locals.session.accountId}"
             data-user-id="${locals.dsConfig.signerEmail}"
             data-clickwrap-id="${clickwrapId}">
        </div>
        <button type="button" class="btn btn-docu" onclick="renderClickwrap()">Show clickwrap</button>
    </c:when>
    <c:otherwise>
        <p>Cannot find any active clickwrap. Please first create a clickwrap using
            <a target="_blank" href="/c001">example 1</a> and activate it using
            <a target="_blank" href="/c002">example 2</a>.
        </p>
    </c:otherwise>
</c:choose>

<script src="https://demo.docusign.net/clickapi/sdk/latest/docusign-click.js"></script>
<script>
    function renderClickwrap() {
        docuSignClick.Clickwrap.render({
            environment: 'https://demo.docusign.net',
            accountId: $("#ds-click").data("account-id"),
            clickwrapId: $("#ds-click").data("clickwrap-id"),
            clientUserId: $("#ds-click").data("user-id")
        }, '#ds-click');
    }
</script>

<jsp:include page="../../../partials/foot.jsp"/>
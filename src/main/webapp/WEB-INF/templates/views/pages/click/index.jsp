<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../partials/head.jsp"/>

<c:if test="${locals.user == null}">
    <!-- IF not signed in -->
    <div>
    <div class="jumbotron jumbotron-fluid"> <table>
        <tbody>
        <tr>
            <td>
                <h1 class="display-4">Java Launcher</h1>
                <p class="Xlead">Welcome to the DocuSign Java examples using multiple OAuth flows (JWT and Authorization Code Grant).</p>
            </td>
            <td>
                <img src="/assets/banner-code.png" />
            </td>
        </tr>
        </tbody>
    </table>
    </div>
</c:if>

<div class="container" style="margin-top: 40px" id="index-page">
    <h2>Welcome</h2>
    <p>This launcher both demonstrates use of common OAuth2 grant flows and multiple usage examples from the DocuSign Rooms REST API.</p>
    <c:if test="${showDoc == true}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> on using OAuth Authorization Code Grant from a Java application.</p>
    </c:if>

    <h2>Click Examples</h2>

    <h4 id="example001">1. <a href="c001">Create a clickwrap</a></h4>
    <p>This topic describes how to use <a target ='_blank' rel="noopener noreferrer" href="https://www.docusign.com/products/click">DocuSign Click</a>
        to create a clickwrap that you can embed in your website or app.<br>
        To learn how to generate a clickwrap automatically by using the UI, see the <a target ='_blank' rel="noopener noreferrer" href="https://support.docusign.com/en/guides/click-user-guide">Click User Guide</a>.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/createclickwrap">ClickWraps:createClickwrap</a>.
    </p>

    <h4 id="example002">2. <a href="c002">Activate a clickwrap</a></h4>
    <p>This topic describes how to use <a target ='_blank' rel="noopener noreferrer" href="https://www.docusign.com/products/click">DocuSign Click</a>
        to activate a new clickwrap that you have already created.
        By default, new clickwraps are inactive. You must activate your clickwrap before you can use it.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/updateclickwrapversion">ClickWraps:updateClickwrapVersion</a>
    </p>

    <h4 id="example003">3. <a href="c003">How to create a new clickwrap version</a></h4>
    <p>This topic demonstrates how to use the Click API to create a new version of a clickwrap.
        You can specify whether you require users who have previously accepted the clickwrap
        to accept the new version when they return to your website.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/updateclickwrapversion">ClickWraps:updateClickwrapVersion</a>
    </p>

    <h4 id="example004">4. <a href="c004">Get a list of clickwraps</a></h4>
    <p>This topic demonstrates how to use the Click API to get a list of clickwraps
        associated with a specific DocuSign user.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/getclickwraps">ClickWraps:getClickwraps</a>
    </p>

    <h4 id="example005">5. <a href="c005">Get clickwrap responses</a></h4>
    <p>This topic demonstrates how to use the Click API to get user responses to
        your clickwrap agreements.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/getclickwrapagreements">ClickWraps:getClickwrapAgreements</a>
    </p>
</div>

<!-- anchor-js is only for the index page -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page="../../partials/foot.jsp"/>

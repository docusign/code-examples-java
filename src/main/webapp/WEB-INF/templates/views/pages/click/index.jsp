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
    <c:if test="${showDoc == true}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> on using OAuth Authorization Code Grant from a Java application.</p>
    </c:if>

    <h4 id="example001">1. <a href="c001">Create a clickwrap</a></h4>
    <p>Creates a clickwrap that you can embed in your website or app.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/createclickwrap">ClickWraps:createClickwrap</a>.
    </p>

    <h4 id="example002">2. <a href="c002">Activate a clickwrap</a></h4>
    <p>Activates a new clickwrap that you have already created.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/updateclickwrapversion">ClickWraps:updateClickwrapVersion</a>
    </p>

    <h4 id="example003">3. <a href="c003">Create a new clickwrap version</a></h4>
    <p>Creates a new version of a clickwrap.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/updateclickwrapversion">ClickWraps:createClickwrapVersion</a>
    </p>

    <h4 id="example004">4. <a href="c004">Get a list of clickwraps</a></h4>
    <p>Gets a list of clickwraps associated with a specific DocuSign user.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/getclickwraps">ClickWraps:getClickwraps</a>
    </p>

    <h4 id="example005">5. <a href="c005">Get clickwrap responses</a></h4>
    <p>Gets user responses to your clickwrap agreements.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/getclickwrapagreements">ClickWraps:getClickwrapAgreements</a>
    </p>
</div>

<!-- anchor-js is only for the index page -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page="../../partials/foot.jsp"/>

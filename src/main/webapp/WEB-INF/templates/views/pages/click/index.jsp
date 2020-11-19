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
    <p>This example demonstrates creating a DocuSign room.
    </p>
    <p>API methods used:
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Roles/Roles/GetRoles">Roles::GetRoles</a> and
        <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/CreateRoom">Rooms::CreateRoom</a>.
    </p>
</div>

<!-- anchor-js is only for the index page -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page="../../partials/foot.jsp"/>

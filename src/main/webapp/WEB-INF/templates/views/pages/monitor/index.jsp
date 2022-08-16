<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../partials/head.jsp'/>

<div>
<div class='jumbotron jumbotron-fluid'> <table>
    <tbody>
    <tr>
        <td>
            <h1 class='display-4'>Java Launcher</h1>
            <p class='Xlead'>Welcome to the Java code examples for DocuSign Monitor API with JWT Grant authentication</p>
        </td>
        <td>
            <img src='/assets/banner-code.png' />
        </td>
    </tr>
    </tbody>
</table>
</div>

<div class='container' style='margin-top: 40px' id='index-page'>
    <c:if test="${showDoc == true}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> on using OAuth Authorization Code Grant from a Java application.</p>
    </c:if>

    <c:forEach var="group" items="${launcherTexts}">
        <h2>${group.getName()}</h2>

        <c:forEach var="example" items="${group.getExamples()}">
            <h4 id="${String.format("m%03d", example.getExampleNumber())}">
                <a href="${String.format("eg%03d", example.getExampleNumber())}">
                        ${example.getExampleName()}
                </a>
            </h4>

            <p>${example.getExampleDescription()}</p>

            <p>
                <c:choose>
                    <c:when test="${example.getLinksToAPIMethod().size() == 1}">
                        <span>API method used:</span>
                    </c:when>
                    <c:otherwise>
                        <span>API methods used:</span>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="link" items="${example.getLinksToAPIMethod()}">
                    <a href="${link.getPath()}">
                            ${link.getPathName()}
                    </a>

                    <c:choose>
                        <c:when test="${example.getLinksToAPIMethod().size() == example.getLinksToAPIMethod().indexOf(link) + 1}">
                            <span>.</span>
                        </c:when>
                        <c:when test="${example.getLinksToAPIMethod().size() - 1 == example.getLinksToAPIMethod().indexOf(link) + 1}">
                            <span>and</span>
                        </c:when>
                        <c:otherwise>
                            <span>,</span>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </p>
        </c:forEach>
    </c:forEach>
</div>

<!-- anchor-js is only for the index page -->
<script src='https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js'></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page='../../partials/foot.jsp'/>


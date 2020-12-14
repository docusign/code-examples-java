<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>3. Test a clickwrap.</h4>

<p>Before you embed a clickwrap in your website or app, you should preview it to make sure
    it appears and behaves the way you want. However, the web page in which you test your
    clickwrap cannot be a local file that you open in a browser. The page must be hosted
    on a web server. The DocuSign Clickwrap Tester takes care of this for you, making it easy
    to preview the behavior and appearance of your clickwrap.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${clickwrapOk == true}">
        <p>The clickwrap you created via example 1 will be tested.</p>
        <a target="_blank" class="btn btn-docu" href="${testClickwrapUrl}">Test clickwrap</a>
    </c:when>
    <c:otherwise>
        <p>Cannot find any active clickwrap. Please first create a clickwrap using
            <a target="_blank" href="/c001">example 1</a> and activate it using
            <a target="_blank" href="/c002">example 2</a>.
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="./partials/head.jsp"/>

<%-- Used for consent redirect sequence --%>
<c:set var="message" value='${requestScope["javax.servlet.error.exception"]}' />
<c:if test='${fn:contains(message, "docusign.com/oauth/auth") }'>
<%-- using ' ' instead of ':' to prevent issue with delimiter and redirect url --%>
    <c:set var="url" value="${fn:split(message, ' ')}" />
    <c:redirect url="${url[1]}${url[2]}" />

</c:if>
<c:if test="${done.fixingInstructions != null }">
    <h2>Problem: an error occurred</h2>

    <p>Error information:</p>
    <p><em>Message: </em>${done.message}</p>
    <p>${done.fixingInstructions}</p>
</c:if>
<c:if test="${done.fixingInstructions == null }">
    <p><strong>Oops, we have encountered a problem.</strong></p>
    <p><em>Message: </em>${done.message}</p>
    <p><em>Dump: </em>${done.stackTracePrinted}</p>

    <p>Please check your account configuration. If you are unable to resolve the problem, <a href="https://github.com/docusign/code-examples-java/issues/new" target="_blank" rel="nofollow noopener noreferrer">open a new issue on GitHub</a>.</p>
</c:if>

<p><a href="/">${launcherTexts.getContinueButton()}</a></p>

<jsp:include page="./partials/foot.jsp"/>

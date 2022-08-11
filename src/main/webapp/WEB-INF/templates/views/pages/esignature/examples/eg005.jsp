<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>List an envelope's recipients and their status</h4>
<p>List the envelope's recipients, including their current status.</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>API method used:
    <a target='_blank' rel="noopener noreferrer"
       href="https://developers.docusign.com/docs/esign-rest-api/reference/envelopes/enveloperecipients/list/">EnvelopeRecipients::list</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${envelopeOk}">
        <p>The envelope you created via Send an envelope with a remote (email) signer and cc recipient. will be queried.</p>

        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Problem: please first create an envelope using <a href="eg002">Send an envelope with a remote (email) signer and cc recipient..</a> <br/>
            Thank you.</p>

        <form class="eg" action="eg002" method="get">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>

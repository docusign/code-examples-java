<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>6. List an envelope's documents</h4>
<p>List the envelope's documents. A <em>Certificate of Completion</em> document
    is also associated with every envelope.
</p>

<p>This method is often used to dynamically create a list of an envelope's documents
    in preparation for enabling your user to download one or more of the documents.</p>
<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeDocuments/list">EnvelopeDocuments::list</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${envelopeOk}">
        <p>The envelope you created via example 2 will be queried.</p>

        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Problem: please first create an envelope using <a href="eg002">example 2.</a> <br/>
            Thank you.</p>

        <form class="eg" action="eg002" method="get">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>

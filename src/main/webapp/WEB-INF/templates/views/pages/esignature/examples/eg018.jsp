<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>18. Get the custom field data for an envelope</h4>
<p>
    Get the data values associated with the envelope itself. The custom data fields enable you to
    add additional meta-data to the envelope. The custom data fields can be set by the <b>Sender</b>
    via the DocuSign web tool, or can be set programmatically. The data can be included in the
    envelope's certificate of completion.
</p>

<p>
    This method is used to read the custom field values from
    an envelope.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>
    API method used:
    <a target='_blank' href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeCustomFields/list">EnvelopeCustomFields::list</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${envelopeOk}">
        <p>
        The last envelope you created with this example launcher will be queried.
        Recommendation:  use example 9, then this example, since example 9 includes many tabs of different types.
        </p>
        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Problem: please first create an envelope using <a href="eg009">example 9.</a> <br/>
            Thank you.</p>

        <form class="eg" action="eg009" method="get">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>

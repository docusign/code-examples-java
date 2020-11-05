<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>7. Download a document from an envelope</h4>
<p>An envelope's documents can be downloaded one by one or as a complete set.</p>
<p>Document download options:</p>
<ul>
    <li><strong>Combined</strong>: a PDF with the combined content of all documents and the
        certificate
    </li>
    <li><strong>Zip Archive</strong>: a ZIP archive that contains all of the PDF documents,
        the certificate, and any .WAV files used for voice authentication.
    </li>
    <li>The envelope's individual documents</li>
    <li>The envelope's Certificate of Completion</li>
</ul>
<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeDocuments/get">EnvelopeDocuments::get</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${not envelopeOk}">
        <p>Problem: please first create an envelope using <a href="eg002">example 2.</a> <br/>
            You will then need to use example 6 to create the list of documents.<br/>
            Thank you.</p>

        <form class="eg" action="eg002" method="get">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:when>
    <c:when test="${not documentsOk}">
        <p>Problem: please first create a list of the envelope's documents using
            <a href="eg006">example 6.</a> <br/>
            Thank you.</p>

        <form class="eg" action="eg006" method="get">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Please choose a document.<br/>
            The document list is from your results for example 6.</p>

        <form class="eg" action="" method="post" data-busy="form-download">
            <div class="form-group">
                <label for="docSelect">Document selection</label>
                <select class="custom-select" id="docSelect"
                        name="docSelect" aria-describedby="emailHelp">
                    <c:forEach begin="0" end="${documentOptions.size()}" varStatus="loop">
                        <option value="${documentOptions[loop.index].documentId}">
                                ${documentOptions[loop.index].text}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>

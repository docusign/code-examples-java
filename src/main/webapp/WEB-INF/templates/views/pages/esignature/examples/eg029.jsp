<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../../partials/head.jsp" />

<h4>29. Applying a brand to an envelope</h4>
<p>
    The envelope includes a pdf document. Anchor text (<a
        href="https://support.docusign.com/en/guides/AutoPlace-New-DocuSign-Experience">AutoPlace</a>)
    is used to position the signing fields in the documents.
</p>
<p>
    DocuSign enables you to customize the eSignature sending and signing experience with your own 
    branding elements-logo, colors, and more-stored as a collection of settings called a brand in 
    your DocuSign account. This code example demonstrates how to create a brand with the eSignature 
    REST API that you subsequently apply to a DocuSign envelope.
</p>
<c:if test="${showDoc}">
    <p>
        <a target='_blank' href='${documentation}'>Documentation</a> about
        this example.
    </p>
</c:if>


<p>
    API method used: <a target='_blank' rel="noopener noreferrer"
        href="https://developers.docusign.com/esign-rest-api/reference/Envelopes/Envelopes/create">Envelopes::create</a>.
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a>
    on GitHub.
</p>

<c:choose>
    <c:when test="${not empty listBrands}">
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="signerEmail">Signer Email</label> <input type="email"
                    class="form-control" id="signerEmail" name="signerEmail"
                    aria-describedby="emailHelp" placeholder="pat@example.com" required
                    value="${locals.dsConfig.signerEmail}"> <small
                    id="emailHelp" class="form-text text-muted">We'll never
                    share your email with anyone else.</small>
            </div>
            <div class="form-group">
                <label for="signerName">Signer Name</label> <input type="text"
                    class="form-control" id="signerName" placeholder="Pat Johnson"
                    name="signerName" value="${locals.dsConfig.signerName}" required>
            </div>
            <div class="form-group">
                <label for="brandId">Brand</label>
                <select id="brandId" name="brandId" class="form-control">
                    <c:forEach items="${listBrands}" var="brand">
                        <option value="${brand.brandId}">${brand.brandName}</option>
                    </c:forEach>
                </select>
                <c:if test="${empty listBrands}">
                    <small class="form-text text-muted">There are no brands
                        created in your account. Create ones at the <a href='eg027'>'Create brand'</a> page.
                    </small>
                </c:if>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Problem: please first create the brand(s) using <a href="eg027">example 27.</a><br/>Thank you.</p>
        <form class="eg" action="eg027" method="get">
            <button type="submit" class="btn btn-docu">Continue</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp" />

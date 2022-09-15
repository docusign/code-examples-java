<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../../partials/head.jsp" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<c:if test="${showDoc}">
    <p>
        <a target='_blank' href='${documentation}'>Documentation</a> about
        this example.
    </p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
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
                        created in your account. Create ones at the <a href='eg028'>'Create brand'</a> page.
                    </small>
                </c:if>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Problem: please first create the brand(s) using <a href="eg028">Create a brand</a><br/>Thank you.</p>
        <form class="eg" action="eg028" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp" />

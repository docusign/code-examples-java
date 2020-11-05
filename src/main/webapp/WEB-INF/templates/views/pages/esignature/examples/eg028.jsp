<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>28. Creating a brand.</h4>

<p>
    The brand includes a Brand Name <a href="https://support.docusign.com/en/guides/ndse-admin-guide-configure-branding">Configure Brands</a>.
</p>
<p>
    DocuSign enables you to customize the eSignature sending and signing experience with your own 
    branding elements-logo, colors, and more-stored as a collection of settings called a brand in 
    your DocuSign account. This code example demonstrates how to create a brand with the 
    eSignature REST API that you subsequently apply to a DocuSign envelope.
</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API method used:
    <a target='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/esign-rest-api/reference/Accounts/AccountBrands/create">AccountBrands::create</a>.
</p>

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>


<form class="eg" action="" method="post" data-busy="form">
    <p>
        <strong>New brand</strong>
    </p>
    <div class="form-group">
        <label for="brandName">Brand name</label>
        <input type="text" class="form-control" id="brandName" name="brandName"
               aria-describedby="info" placeholder="New Brand" required>
    </div>
    <div class="form-group">
        <label for="language">Brand language</label>
        <select id="language" name="language" class="form-control">
            <c:forEach items="${listLanguage}" var="language">
                <option value="${language.code}">${language.name}</option>
            </c:forEach>
        </select>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
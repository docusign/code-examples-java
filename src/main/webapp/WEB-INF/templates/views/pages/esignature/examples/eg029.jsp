<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../../partials/head.jsp" />

<c:set var="formNumber" value="0" scope="page" />
<c:set var="signerNameInputNumber" value="1" scope="page" />
<c:set var="signerEmailInputNumber" value="0" scope="page" />
<c:set var="brandInputNumber" value="2" scope="page" />
<c:set var="redirectTo28CodeExample" value="href='eg028'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

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
                <label for="signerEmail">
                        ${example.getForms().get(formNumber).getInputs().get(signerEmailInputNumber).getInputName()}
                </label>

                <input type="email"
                       class="form-control"
                       id="signerEmail"
                       name="signerEmail"
                       aria-describedby="emailHelp"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
                       required
                    value="${locals.dsConfig.signerEmail}">

                <small id="emailHelp" class="form-text text-muted">
                        ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
                </small>
            </div>
            <div class="form-group">
                <label for="signerName">
                        ${example.getForms().get(formNumber).getInputs().get(signerNameInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="signerName"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
                       name="signerName"
                       value="${locals.dsConfig.signerName}"
                       required>
            </div>
            <div class="form-group">
                <label for="brandId">
                        ${example.getForms().get(formNumber).getInputs().get(brandInputNumber).getInputName()}
                </label>

                <select id="brandId" name="brandId" class="form-control">
                    <c:forEach items="${listBrands}" var="brand">
                        <option value="${brand.brandId}">${brand.brandName}</option>
                    </c:forEach>
                </select>
                <c:if test="${empty listBrands}">
                    <small class="form-text text-muted">
                        ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectTo28CodeExample)}
                    </small>
                </c:if>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectTo28CodeExample)}</p>

        <form class="eg" action="eg028" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp" />

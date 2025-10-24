<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="instanceNameInputNumber" value="0" scope="page" />
<c:set var="signerEmailInputNumber" value="1" scope="page" />
<c:set var="signerNameInputNumber" value="2" scope="page" />
<c:set var="ccEmailInputNumber" value="3" scope="page" />
<c:set var="ccNameInputNumber" value="4" scope="page" />
<c:set var="redirectToEightCodeExample" value="href='eg008'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>

<c:choose>
    <c:when test="${publishLink == null}">
        <p>${example.getExampleDescription()}</p>
        <c:if test="${showDoc}">
            <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
        </c:if>

        <jsp:include page="../../links_to_api_methods.jsp" />

        <p>
                ${viewSourceFile}
        </p>

        <c:choose>
            <c:when test="${templateId == null && workflowId == null}">
                <p>
                        ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToEightCodeExample)}
                </p>

                <form class="eg" action="eg008" method="get">
                    <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
                </form>
            </c:when>
            <c:otherwise>
                <p>${example.getForms().get(formNumber).getFormName()}</p>

                <form class="eg" action="" method="post" data-busy="form">
                    <div class="form-group">
                        <label for="instanceName">
                                ${example.getForms().get(formNumber).getInputs().get(instanceNameInputNumber).getInputName()}
                        </label>

                        <input type="text"
                               class="form-control"
                               id="instanceName"
                               name="instanceName"
                               placeholder="${example.getForms().get(formNumber).getInputs().get(instanceNameInputNumber).getInputPlaceholder()}"
                               required>
                    </div>

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
                        <label for="ccEmail">
                                ${example.getForms().get(formNumber).getInputs().get(ccEmailInputNumber).getInputName()}
                        </label>

                        <input type="email"
                               class="form-control"
                               id="ccEmail"
                               name="ccEmail"
                               aria-describedby="emailHelp"
                               placeholder="${example.getForms().get(formNumber).getInputs().get(ccEmailInputNumber).getInputPlaceholder()}"
                               required>

                        <small id="emailHelp" class="form-text text-muted">
                                ${launcherTexts.getHelpingTexts().getCCEmailShouldDifferFromSigner()}
                        </small>
                    </div>

                    <div class="form-group">
                        <label for="ccName">
                                ${example.getForms().get(formNumber).getInputs().get(ccNameInputNumber).getInputName()}
                        </label>

                        <input type="text"
                               class="form-control"
                               id="ccName"
                               placeholder="${example.getForms().get(formNumber).getInputs().get(ccNameInputNumber).getInputPlaceholder()}"
                               name="ccName"
                               required>
                    </div>

                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
                </form>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <p>
                ${publishLink}
        </p>
        <form class="eg" action="mae001" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>
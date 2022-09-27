<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="firstFormNumber" value="0" scope="page" />
<c:set var="secondFormNumber" value="1" scope="page" />
<c:set var="signerEmailInputNumber" value="0" scope="page" />
<c:set var="signerNameInputNumber" value="1" scope="page" />
<c:set var="ccEmailInputNumber" value="2" scope="page" />
<c:set var="ccNameInputNumber" value="3" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>


<form class="eg" action="" method="post" data-busy="form">
    <div class="row">
        <div class="col-md-6">
            <h5>${example.getForms().get(firstFormNumber).getFormName()}</h5>
            <div class="form-group">
                <label for="signerEmail">
                    ${example.getForms().get(firstFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}
                </label>

                <input type="email"
                       class="form-control"
                       id="signerEmail"
                       name="signerEmail"
                       aria-describedby="emailHelp"
                       placeholder="${example.getForms().get(firstFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
                       required
                       value="${locals.dsConfig.signerEmail}">
            </div>

            <div class="form-group">
                <label for="signerName">
                    ${example.getForms().get(firstFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}
                </label>

                <input type="text"
                       class="form-control"
                       id="signerName"
                       placeholder="${example.getForms().get(firstFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
                       name="signerName"
                       value="${locals.dsConfig.signerName}"
                       required>
            </div>

            <div class="form-group">
                <label for="ccEmail">
                    ${example.getForms().get(firstFormNumber).getInputs().get(ccEmailInputNumber).getInputPlaceholder()}
                </label>

                <input type="email"
                       class="form-control"
                       id="ccEmail"
                       name="ccEmail"
                       aria-describedby="emailHelp"
                       placeholder="${example.getForms().get(firstFormNumber).getInputs().get(ccEmailInputNumber).getInputPlaceholder()}"
                       required>
            </div>

            <div class="form-group">
                <label for="ccName">
                    ${example.getForms().get(firstFormNumber).getInputs().get(ccNameInputNumber).getInputPlaceholder()}
                </label>
                <input type="text"
                       class="form-control"
                       id="ccName"
                       placeholder="${example.getForms().get(firstFormNumber).getInputs().get(ccNameInputNumber).getInputPlaceholder()}"
                       name="ccName"
                       required>
            </div>
        </div>

        <div class="col-md-6">
            <h5>${example.getForms().get(secondFormNumber).getFormName()}</h5>
            <div class="form-group">
                <label for="signerEmail2">
                    ${example.getForms().get(secondFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}
                </label>

                <input type="email"
                       class="form-control"
                       id="signerEmail2"
                       name="signerEmail2"
                       aria-describedby="emailHelp"
                       placeholder="${example.getForms().get(secondFormNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
                       required
                       value="${locals.dsConfig.signerEmail}">
            </div>

            <div class="form-group">
                <label for="signerName2">
                    ${example.getForms().get(secondFormNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}
                </label>

                <input type="text"
                       class="form-control"
                       id="signerName2"
                       placeholder="${example.getForms().get(secondFormNumber).getInputs().get(v).getInputPlaceholder()}"
                       name="signerName2"
                       value="${locals.dsConfig.signerName}"
                       required>
            </div>

            <div class="form-group">
                <label for="ccEmail2">
                    ${example.getForms().get(secondFormNumber).getInputs().get(ccEmailInputNumber).getInputPlaceholder()}
                </label>

                <input type="email"
                       class="form-control"
                       id="ccEmail2"
                       name="ccEmail2"
                       aria-describedby="emailHelp"
                       placeholder="${example.getForms().get(secondFormNumber).getInputs().get(ccEmailInputNumber).getInputPlaceholder()}"
                       required>
            </div>

            <div class="form-group">
                <label for="ccName2">
                    ${example.getForms().get(secondFormNumber).getInputs().get(ccNameInputNumber).getInputPlaceholder()}
                </label>

                <input type="text"
                       class="form-control"
                       id="ccName2"
                       placeholder="${example.getForms().get(secondFormNumber).getInputs().get(ccNameInputNumber).getInputPlaceholder()}"
                       name="ccName2"
                       required>
            </div>
        </div>
        </div>
    <div>
        <input type="hidden" name="csrf_token" value="{{ csrf_token() }}"/>
        <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
    </div>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

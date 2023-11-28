<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="hmacInputNumber" value="0" scope="page" />
<c:set var="jsonInputNumber" value="1" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>

<p>
    <b>Prerequisites:</b> See <a href="https://developers.docusign.com/platform/webhooks/connect/validate/">How to validate a webhook message using HMAC</a>.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group" style="display: flex;">
        <label for="hmacSecret" style="width: 50%;">
            ${example.getForms().get(formNumber).getInputs().get(hmacInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="hmacSecret"
               name="hmacSecret"
               required/>
    </div>
    <div class="form-group">
        <label for="jsonPayload">
            ${example.getForms().get(formNumber).getInputs().get(jsonInputNumber).getInputName()}
        </label>

        <textarea
                rows="6"
                class="form-control"
                id="jsonPayload"
                name="jsonPayload"
                required ></textarea>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

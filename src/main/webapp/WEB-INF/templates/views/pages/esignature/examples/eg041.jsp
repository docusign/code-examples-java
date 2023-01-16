<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="signerEmailInputNumber" value="0" scope="page" />
<c:set var="signerNameInputNumber" value="1" scope="page" />
<c:set var="countryCodeInputNumber" value="2" scope="page" />
<c:set var="phoneNumberInputNumber" value="3" scope="page" />

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
      <label for="countryCode">
          ${example.getForms().get(formNumber).getInputs().get(countryCodeInputNumber).getInputName()}
      </label>

      <input type="tel"
             class="form-control"
             id="countryCode"
             name="countryCode"
             aria-describedby="accessHelp"
             placeholder="${example.getForms().get(formNumber).getInputs().get(countryCodeInputNumber).getInputPlaceholder()}"
             required/>

      <small id="accessHelp" class="form-text text-muted">
          ${launcherTexts.getHelpingTexts().getCountryCodeText()}
      </small>
    </div>
  <div class="form-group">
      <label for="phoneNumber">
          ${example.getForms().get(formNumber).getInputs().get(phoneNumberInputNumber).getInputName()}
      </label>

      <input type="text"
             class="form-control"
             id="phoneNumber"
             placeholder="${example.getForms().get(formNumber).getInputs().get(phoneNumberInputNumber).getInputPlaceholder()}"
             name="phoneNumber"
             value="" required>

      <small id="emailHelp" class="form-text text-muted">
          ${launcherTexts.getHelpingTexts().getPhoneNumberWillBeNotified()} ${launcherTexts.getHelpingTexts().getPhoneNumberWontBeShared()}
      </small>
  </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

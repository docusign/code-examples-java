<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="countryCodeInputNumber" value="0" scope="page" />
<c:set var="numberInputNumber" value="1" scope="page" />
<c:set var="signerNameInputNumber" value="2" scope="page" />
<c:set var="deliveryMethod" value="6" scope="page" />
<c:set var="ccCountryCodeInputNumber" value="3" scope="page" />
<c:set var="ccNumberInputNumber" value="4" scope="page" />
<c:set var="ccSignerNameInputNumber" value="5" scope="page" />
<c:set var="sms" value="7" scope="page" />
<c:set var="whatsapp" value="8" scope="page" />


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
      <label for="deliveryMethod">${example.getForms().get(formNumber).getInputs().get(deliveryMethod).getInputName()}</label><br>
      <input type="radio" checked="checked" name="deliveryMethod" id="delivery_method1" value="SMS"/> ${example.getForms().get(formNumber).getInputs().get(sms).getInputName()} &nbsp;
      <input type="radio" name="deliveryMethod" id="delivery_method2" value="WhatsApp"/> ${example.getForms().get(formNumber).getInputs().get(whatsapp).getInputName()} <br>
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
            ${example.getForms().get(formNumber).getInputs().get(numberInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="phoneNumber"
               placeholder="${example.getForms().get(formNumber).getInputs().get(numberInputNumber).getInputPlaceholder()}"
               name="phoneNumber"
               value="" required>

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getPhoneNumberWillBeNotified()} ${launcherTexts.getHelpingTexts().getPhoneNumberWontBeShared()}
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
        <label for="ccCountryCode">
            ${example.getForms().get(formNumber).getInputs().get(ccCountryCodeInputNumber).getInputName()}
        </label>

        <input type="tel"
               class="form-control"
               id="ccCountryCode"
               name="ccCountryCode"
               aria-describedby="accessHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(ccCountryCodeInputNumber).getInputPlaceholder()}"
               required />

        <small id="accessHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getCountryCodeText()}
        </small>
    </div> 
    <div class="form-group">
        <label for="ccPhoneNumber">
            ${example.getForms().get(formNumber).getInputs().get(ccNumberInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="ccPhoneNumber"
               placeholder="${example.getForms().get(formNumber).getInputs().get(ccNumberInputNumber).getInputPlaceholder()}"
               name="ccPhoneNumber"
               value=""
               required>

        <small id="emailHelp" class="form-text text-muted">
            ${launcherTexts.getHelpingTexts().getPhoneNumberWillBeNotified()} ${launcherTexts.getHelpingTexts().getPhoneNumberWontBeShared()}
        </small>
    </div>
    <div class="form-group">
        <label for="ccName">
            ${example.getForms().get(formNumber).getInputs().get(ccSignerNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="ccName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(ccSignerNameInputNumber).getInputPlaceholder()}"
               name="ccName"
               required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

</br>
</br>
</br>


<jsp:include page="../../../partials/foot.jsp"/>

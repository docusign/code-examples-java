<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

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
        <label for="countryCode">Signer Country Code</label>
        <input type="tel" class="form-control" id="countryCode" name="countryCode"
              aria-describedby="accessHelp" placeholder="1" required/>
        <small id="accessHelp" class="form-text text-muted">The country code for the phone number below.</small>
      </div>  
    <div class="form-group">
        <label for="phoneNumber">Signer Phone Number</label>
        <input type="text" class="form-control" id="phoneNumber" placeholder="415-555-1212" name="phoneNumber"
               value="" required>
        <small id="emailHelp" class="form-text text-muted">This phone number will receive a notification. We'll never share your phone number with anyone else.</small>
    </div>
    <div class="form-group">
        <label for="signerName">Signer Name</label>
        <input type="text" class="form-control" id="signerName" placeholder="Pat Johnson" name="signerName"
               value="${locals.dsConfig.signerName}" required>
    </div>
    <div class="form-group">
        <label for="ccCountryCode">CC Country Code</label>
        <input type="tel" class="form-control" id="ccCountryCode" name="ccCountryCode"
              aria-describedby="accessHelp" placeholder="1" required />
        <small id="accessHelp" class="form-text text-muted">The country code for the phone number below.</small>
    </div> 
    <div class="form-group">
        <label for="ccPhoneNumber">CC Phone Number</label>
        <input type="text" class="form-control" id="ccPhoneNumber" placeholder="415-555-1212" name="ccPhoneNumber"
               value="" required>
        <small id="emailHelp" class="form-text text-muted">This phone number will receive a notification. We'll never share your phone number with anyone else.</small>
    </div>
    <div class="form-group">
        <label for="ccName">CC Name</label>
        <input type="text" class="form-control" id="ccName" placeholder="Pat Johnson" name="ccName"
               required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

</br>
</br>
</br>


<jsp:include page="../../../partials/foot.jsp"/>

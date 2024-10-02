<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="emailInputNumber" value="0" scope="page" />
<c:set var="firstNameInputNumber" value="1" scope="page" />
<c:set var="lastNameInputNumber" value="2" scope="page" />

<h4>13. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
   ${viewSourceFile}
</p>

<form class="eg" method="post" data-busy="form">
    <div class="form-group">
        <label for="email">
            ${example.getForms().get(formNumber).getInputs().get(emailInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="email"
               name="email"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(emailInputNumber).getInputPlaceholder()}"
               required>
    </div>

    <div class="form-group">
        <label for="firstName">
            ${example.getForms().get(formNumber).getInputs().get(firstNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="firstName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(firstNameInputNumber).getInputPlaceholder()}"
               name="firstName"
               required />
    </div>

    <div class="form-group">
        <label for="lastName">
            ${example.getForms().get(formNumber).getInputs().get(lastNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="lastName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(lastNameInputNumber).getInputPlaceholder()}"
               name="lastName"
               required />
    </div>

    <input type="hidden" name="csrf_token" value="${csrfToken}"/>
    <button type="submit" class="btn btn-primary">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page='../../../partials/foot.jsp'/>

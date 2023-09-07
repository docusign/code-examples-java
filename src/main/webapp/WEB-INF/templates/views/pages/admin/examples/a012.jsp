<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="sourceAccountIdInputNumber" value="0" scope="page" />
<c:set var="targetAccountNameInputNumber" value="1" scope="page" />
<c:set var="targetAccountFirstNameInputNumber" value="2" scope="page" />
<c:set var="targetAccountLastNameInputNumber" value="3" scope="page" />
<c:set var="targetAccountEmailInputNumber" value="4" scope="page" />

<h4>12. ${example.getExampleName()}</h4>
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
        <label for="sourceAccountId">
            ${example.getForms().get(formNumber).getInputs().get(sourceAccountIdInputNumber).getInputName()}
        </label>

        <select id="sourceAccountId" name="sourceAccountId" class="form-control">
            <c:forEach items="${groups}" var="group">
                <option value="${group.getAccountId()}">${group.getAccountName()}</option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="targetAccountName">
            ${example.getForms().get(formNumber).getInputs().get(targetAccountNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="targetAccountName"
               name="targetAccountName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(targetAccountNameInputNumber).getInputPlaceholder()}"
               required>
    </div>

    <div class="form-group">
        <label for="targetAccountFirstName">
            ${example.getForms().get(formNumber).getInputs().get(targetAccountFirstNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="targetAccountFirstName"
               name="targetAccountFirstName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(targetAccountFirstNameInputNumber).getInputPlaceholder()}"
               required>
    </div>

    <div class="form-group">
        <label for="targetAccountLastName">
            ${example.getForms().get(formNumber).getInputs().get(targetAccountLastNameInputNumber).getInputName()}
        </label>

        <input type="text"
               class="form-control"
               id="targetAccountLastName"
               name="targetAccountLastName"
               placeholder="${example.getForms().get(formNumber).getInputs().get(targetAccountLastNameInputNumber).getInputPlaceholder()}"
               required>
    </div>

    <div class="form-group">
        <label for="targetAccountEmail">
            ${example.getForms().get(formNumber).getInputs().get(targetAccountEmailInputNumber).getInputName()}
        </label>

        <input type="email"
               class="form-control"
               id="targetAccountEmail"
               name="targetAccountEmail"
               aria-describedby="emailHelp"
               placeholder="${example.getForms().get(formNumber).getInputs().get(targetAccountEmailInputNumber).getInputPlaceholder()}"
               required>
    </div>

    <input type="hidden" name="csrf_token" value="${csrfToken}"/>
    <button type="submit" class="btn btn-primary">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page='../../../partials/foot.jsp'/>

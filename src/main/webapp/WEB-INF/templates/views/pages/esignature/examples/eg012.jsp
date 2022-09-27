<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="viewInputNumber" value="0" scope="page" />
<c:set var="redirectToSecondCodeExample" value="href='eg002'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>
<c:if test="${not envelopeOk}">
    <p>${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToSecondCodeExample)}</p>
</c:if>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="startingView">
            ${example.getForms().get(formNumber).getInputs().get(viewInputNumber).getInputName()}
        </label>

        <select id="startingView" name="startingView" class="form-control">
            <option value="frontPage" selected>Front page</option>
            <option value="envelope" ${envelopeOk ? '' : 'disabled="true"'} >
                The envelope's documents view
            </option>
        </select>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

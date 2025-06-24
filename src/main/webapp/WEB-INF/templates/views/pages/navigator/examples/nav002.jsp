<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:include page="../../../partials/head.jsp" />

    <c:set var="formNumber" value="0" scope="page" />
    <c:set var="agreementsInputNumber" value="0" scope="page" />

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
            <label for="agreementId">
                ${example.getForms().get(formNumber).getInputs().get(agreementsInputNumber).getInputName()}
            </label>

            <select id="agreementId" name="agreementId" class="form-control">
                <c:forEach var="app" items="${agreements}">
                    <option value="${app.id().get()}">${app.fileName().get()}</option>
                </c:forEach>
            </select>
        </div>
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
    </form>

    <jsp:include page="../../../partials/foot.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="documentInputNumber" value="0" scope="page" />
<c:set var="redirectToSecondCodeExample" value="href='eg002'" scope="page" />
<c:set var="redirectSecondNumber" value="0" scope="page" />
<c:set var="redirectToSixthCodeExample" value="href='eg006'" scope="page" />
<c:set var="redirectSixNumber" value="1" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${not envelopeOk}">
        <p>${example.getRedirectsToOtherCodeExamples().get(redirectSecondNumber).getRedirectText().replaceFirst("\\{0}", redirectToSecondCodeExample)}</p>

        <form class="eg" action="eg002" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:when>
    <c:when test="${not documentsOk}">
        <p>${example.getRedirectsToOtherCodeExamples().get(redirectSixNumber).getRedirectText().replaceFirst("\\{0}", redirectToSixthCodeExample)}</p>

        <form class="eg" action="eg006" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>${example.getForms().get(formNumber).getFormName()}</p>

        <form class="eg" action="" method="post" data-busy="form-download">
            <div class="form-group">
                <label for="docSelect">
                        ${example.getForms().get(formNumber).getInputs().get(documentInputNumber).getInputName()}
                </label>

                <select class="custom-select" id="docSelect"
                        name="docSelect" aria-describedby="emailHelp">
                    <c:forEach begin="0" end="${documentOptions.size() - 1}" varStatus="loop">
                        <option value="${documentOptions[loop.index].documentId}">
                                ${documentOptions[loop.index].text}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>

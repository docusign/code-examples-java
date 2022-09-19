<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="permissionInputNumber" value="0" scope="page" />
<c:set var="redirectToSecondCodeExample" value="href='a002'" scope="page" />
<c:set var="redirectNumber" value="0" scope="page" />

<h4>9. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
   ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${emailAddress != null }">
        <p>${example.getForms().get(formNumber).getFormName().replaceFirst("\\{0}", emailAddress)}</p>

        <form class="eg" method="post" data-busy="form">
            <div class="form-group">
                <label for="Products">
                        ${example.getForms().get(formNumber).getInputs().get(permissionInputNumber).getInputName()}
                </label>

                <select id="Products" name="ProductId" class="form-control">
                    <c:forEach var="entry" items="${listProducts}">
                        <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
                    </c:forEach>
                </select>
            </div>

            <input type="hidden" name="csrf_token" value="${csrfToken}"/>
            <button type="submit" class="btn btn-primary">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToSecondCodeExample)}
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page='../../../partials/foot.jsp'/>

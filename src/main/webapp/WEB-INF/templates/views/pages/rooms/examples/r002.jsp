<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="roomNameInputNumber" value="0" scope="page" />
<c:set var="templateInputNumber" value="1" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />

<c:choose>
    <c:when test="${templateList.size() == 0}">
        <p>
        ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText()}
        </p>
    </c:when>
    <c:otherwise>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="roomName">
                        ${example.getForms().get(formNumber).getInputs().get(roomNameInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="roomName"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(roomNameInputNumber).getInputPlaceholder()}"
                       name="roomName"
                       required>
            </div>
            <div class="form-group">
                <label for="roomTemplate">
                        ${example.getForms().get(formNumber).getInputs().get(templateInputNumber).getInputName()}
                </label>

                <select id="roomTemplate" name="roomTemplateId" class="form-control">
                    <c:forEach items="${templateList}" var="template">
                        <option value="${template.roomTemplateId}">${template.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
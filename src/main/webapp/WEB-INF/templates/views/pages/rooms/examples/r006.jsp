<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="1" scope="page" />
<c:set var="documentInputNumber" value="0" scope="page" />
<c:set var="officeInputNumber" value="1" scope="page" />
<c:set var="redirectToForthCodeExample" value="href='r004'" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='r001'" scope="page" />
<c:set var="redirectNumber" value="1" scope="page" />
<c:set var="redirectNoDocumentsNumber" value="0" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />

    <c:choose>
        <c:when test="${roomsList == null || roomsList.size() == 0}">
            ${example.getRedirectsToOtherCodeExamples().get(redirectNumber).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}

            <form class="eg" action="/r001" method="get" >
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
            </form>
        </c:when>

        <c:when test="${roomsList != null && documentsList == null}">
            <form class="eg" method="GET" data-busy="form">
                <div id="Rooms">
                    <div class="form-group">
                        <p>${example.getForms().get(formNumber).getFormName()}</p>

                        <label for="roomsListId">
                                ${example.getForms().get(formNumber).getInputs().get(documentInputNumber).getInputName()}
                        </label>
                        <select id="roomsListId" name="roomId" class="form-control">
                            <c:forEach items="${roomsList}" var="room">
                                <option value="${room.roomId}">${room.name}</option>
                            </c:forEach>
                        </select>
                        <br/>
                        <input type="hidden" name="_csrf" value="${csrfToken}">
                        <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
                    </div>
                </div>
            </form>
        </c:when>

        <c:when test="${documentsList != null && documentsList.size() == 0}">
            ${example.getRedirectsToOtherCodeExamples().get(redirectNoDocumentsNumber).getRedirectText().replaceFirst("\\{0}", redirectToForthCodeExample)}
            <form class="eg" action="/r004" method="get">
                <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
            </form>
        </c:when>

        <c:when test="${documentsList != null && documentsList.size() != 0}">
            <form class="eg" action="" method="POST" data-busy="form">
                <div id="documentsForRooms">
                    <div class="form-group">
                        <p>${example.getForms().get(formDocumentsNumber).getFormName()}</p>
                        <label for="documentsList">
                                ${example.getForms().get(formDocumentsNumber).getInputs().get(documentInputNumber).getInputName()}
                        </label>

                        <select id="documentsList" name="DocumentId" class="form-control">
                            <c:forEach items="${documentsList}" var="document">
                                <option value="${document.docuSignFormId}">${document.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
                </div>
            </form>
        </c:when>
    </c:choose>


<jsp:include page="../../../partials/foot.jsp"/>
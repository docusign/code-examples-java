<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formOneNumber" value="0" scope="page" />
<c:set var="formTwoNumber" value="1" scope="page" />
<c:set var="documentsInputNumber" value="0" scope="page" />
<c:set var="redirectToForthCodeExample" value="href='r004'" scope="page" />
<c:set var="redirectForthNumber" value="0" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='r001'" scope="page" />
<c:set var="redirectFirstNumber" value="1" scope="page" />

<h4>6. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />


<form class="eg" action="" method="post" data-busy="form">
    <c:choose>
        <c:when test="${roomList!=null}">
            <div class="form-group">
                <label for="roomId">
                    ${example.getForms().get(formTwoNumber).getInputs().get(documentsInputNumber).getInputName()}
                </label>

                <select id="roomId" name="roomId" class="form-control">
                    <option value="" label="Please select the room" disabled aria-selected="true"></option>
                    <c:forEach items="${roomList}" var="room">
                        <option value="${room.roomId}">${room.name}</option>
                    </c:forEach>
                </select>
            </div>
        </c:when>
        <c:otherwise>
            ${example.getRedirectsToOtherCodeExamples().get(redirectFirstNumber).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${formList !=null}">
            <c:choose>
                <c:when test="${formList[0].libraryFormId !=null}">
                    <div class="form-group">
                        <label for="formId">
                            ${example.getForms().get(formOneNumber).getInputs().get(documentsInputNumber).getInputName()}
                        </label>

                        <select id="formId" name="formId" class="form-control">
                            <c:forEach items="${formList}" var="form">
                                <option value="${form.libraryFormId}" selected>${form.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:when>
                <c:otherwise>
                    ${example.getRedirectsToOtherCodeExamples().get(redirectForthNumber).getRedirectText().replaceFirst("\\{0}", redirectToForthCodeExample)}
                </c:otherwise>
            </c:choose>
        </c:when>
    </c:choose>


    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
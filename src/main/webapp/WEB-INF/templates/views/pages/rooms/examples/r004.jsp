<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>4. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />


<form class="eg" action="" method="post" data-busy="form">
    <c:choose>
        <c:when test="${roomList!=null}">
            <div class="form-group">
                <label for="roomId">Room</label>
                <select id="roomId" name="roomId" class="form-control">
                    <option value="" label="Please select the room" disabled aria-selected="true"></option>
                    <c:forEach items="${roomList}" var="room">
                        <option value="${room.roomId}">${room.name}</option>
                    </c:forEach>
                </select>
            </div>
        </c:when>
        <c:otherwise>
            <p>Problem: please first create a room using <a href="r001">example 1</a> </p>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${formList !=null}">
            <div class="form-group">
                <label for="formId">Form</label>
                <c:choose>
                    <c:when test="${formList[0].libraryFormId !=null}">
                        <select id="formId" name="formId" class="form-control">
                            <c:forEach items="${formList}" var="form">
                                <option value="${form.libraryFormId}" selected>${form.name}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <p>Problem: this room have already all the form </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>
    </c:choose>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>


<jsp:include page="../../../partials/foot.jsp"/>
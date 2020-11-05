<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>6. Creating an external form fill session.</h4>

<p>The DocuSign Rooms API offers developers the option to have users fill out forms without
    logging in to the Rooms UI through the use of an <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides/forms">external form fill session</a>.
    This example demonstrates how to create an external form fill session using the
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides">Rooms API</a>:
    the result of this code example is the URL for the form fill session, which you can embed in
    your integration or send to the user.
</p>

<p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a>,
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Forms/FormLibraries/GetFormLibraries">FormLibraries::GetFormLibraries</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Forms/ExternalFormFillSessions/CreateExternalFormFillSession">ExternalFormFillSessions::CreateExternalFormFillSession</a>,
</p>


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
            <p>Problem: please first create a room using <a href="r001.jsp">example 1</a></p>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${formList !=null}">
            <c:choose>
                <c:when test="${formList[0].libraryFormId !=null}">
                    <div class="form-group">
                        <label for="formId">Form</label>
                        <select id="formId" name="formId" class="form-control">
                            <c:forEach items="${formList}" var="form">
                                <option value="${form.libraryFormId}" selected>${form.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:when>
                <c:otherwise>
                    <p>Problem: selected rooms does not have any documents. Please first add form to a room using <a href="r004">example 4</a></p>
                    <p>Thank you</p>
                </c:otherwise>
            </c:choose>
        </c:when>
    </c:choose>


    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
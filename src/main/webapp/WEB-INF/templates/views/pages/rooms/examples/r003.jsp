<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>3. Exporting data from a room.</h4>

<p>This example demonstrates how to export rooms data from a <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides">DocuSign Room</a>.
</p>

<p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRoomFieldData">Rooms::GetRoomFieldSet</a>.
</p>

<c:choose>
    <c:when test="${roomList.size() == 0}">
        <p>Cannot find any rooms. Please first create a room using
            <a target='_blank' href='/r001'>example 1</a> or
            <a target='_blank' href='/r002'>example 2</a>.
        </p>
    </c:when>
    <c:otherwise>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="roomId">Room</label>
                <select id="roomId" name="roomId" class="form-control">
                    <c:forEach items="${roomList}" var="room">
                        <option value="${room.roomId}">${room.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>2. Creating a room with a template.</h4>

<p>This example demonstrates creating a DocuSign Room using a predefined template.
    If you've <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides/templates">created a template</a>
    for your organization in either Rooms for Mortgage or Rooms for Real Estate,
    you can create rooms based on this template using the Rooms API.
</p>

<p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/RoomTemplates/RoomTemplates/GetRoomTemplates">RoomTemplates::GetRoomTemplates</a>,
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Roles/Roles/GetRoles">Roles::GetRoles</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/CreateRoom">Rooms::CreateRoom</a>.
</p>

<c:choose>
    <c:when test="${templateList.size() == 0}">
        <p>Cannot find any templates. Please first <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides/templates#creating-a-room-template">create a template</a>.
        </p>
    </c:when>
    <c:otherwise>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="roomName">Room Name</label>
                <input type="text" class="form-control" id="roomName" placeholder="Please write room name" name="roomName" required>
            </div>
            <div class="form-group">
                <label for="roomTemplate">Template</label>
                <select id="roomTemplate" name="roomTemplateId" class="form-control">
                    <c:forEach items="${templateList}" var="template">
                        <option value="${template.roomTemplateId}">${template.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>1. Creating a room.</h4>

<p>This example demonstrates creating a DocuSign room.</p>

<p>API methods used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Roles/Roles/GetRoles">Roles::GetRoles</a> and
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/CreateRoom">Rooms::CreateRoom</a>.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="roomName">Room Name</label>
        <input type="text" class="form-control" id="roomName" placeholder="Please write room name" name="roomName" required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>5. Get Rooms with filters.</h4>

<p>This example demonstrates how to return rooms filtered by your parameters using the
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides">Rooms API</a>.
    This specific code example filters for all rooms that have had their field data, updated within
    the date range, as shown on the Details tab in the UI.
</p>

<p>API method used:
    <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/reference/Rooms/Rooms/GetRooms">Rooms::GetRooms</a>.
</p>

<form class="eg" action="" method="post" data-busy="form">

    <div class="form-group">
        <label for="startDate">Data Changed Start Date</label>
        <input type="date" name="startDate" id="startDate"
               value="${startDate}" class="form-control" placeholder="yyyy-MM-dd">
    </div>
    <div class="form-group">
        <label for="endDate">Data Changed End Date</label>
        <input type="date" name="endDate" id="endDate"
               value="${endDate}" class="form-control" placeholder="yyyy-MM-dd">
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
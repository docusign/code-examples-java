<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>1. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<p>
    ${viewSourceFile}
</p>
<jsp:include page="../../links_to_api_methods.jsp" />

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="roomName">Room Name</label>
        <input type="text" class="form-control" id="roomName" placeholder="Please write room name" name="roomName" required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
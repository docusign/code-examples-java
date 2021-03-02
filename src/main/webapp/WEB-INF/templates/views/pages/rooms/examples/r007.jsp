<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>7. Creating a form group.</h4>

<p>This example demonstrates creating a form group for your DocuSign Rooms for Real Estate account.</p>

<p>API methods used:
    <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/rooms-api/reference/forms/formgroups/createformgroup">FormGroups:CreateFormGroup</a>.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="formGroupName">Form Group Name</label>
        <input type="text" class="form-control" id="formGroupName" placeholder="Please write form group name" name="formGroupName" required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
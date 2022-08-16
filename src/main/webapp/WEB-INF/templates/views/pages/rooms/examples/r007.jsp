<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>7. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="formGroupName">Form Group Name</label>
        <input type="text" class="form-control" id="formGroupName" placeholder="Please write form group name" name="formGroupName" required>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
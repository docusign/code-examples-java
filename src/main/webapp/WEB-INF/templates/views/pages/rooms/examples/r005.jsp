<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>5. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />

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
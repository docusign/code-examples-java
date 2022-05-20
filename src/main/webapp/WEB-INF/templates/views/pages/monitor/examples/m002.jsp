<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>2. Query monitoring data with filters</h4>
<p>Demonstrates how to query an organization's data based on specified filters and aggregations.</p>
<p>
    API method used:
    <a target='_blank' href="https://developers.docusign.com/docs/monitor-api/reference/monitor/dataset/postwebquery/">DataSet:postWebQuery</a>.
</p>
<p>
    View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<p>
    Please select start and end dates.
</p>

<form class="eg" action="" method="post" data-busy="form">

    <div class="form-group">
        <label for="startDate">Start date</label>
        <input type="date" name="startDate" id="startDate"
               value="${startDate}" class="form-control" placeholder="yyyy-MM-dd">
    </div>
    <div class="form-group">
        <label for="endDate">End date</label>
        <input type="date" name="endDate" id="endDate"
               value="${endDate}" class="form-control" placeholder="yyyy-MM-dd">
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Submit</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>
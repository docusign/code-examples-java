<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>3a. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>
<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${exportOk == true}">
        <p>Uses the export information that was received during previous step</p>
        <form class="eg" action="" method="post" data-busy="form">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            Problem: please first bulk export user data using <a target="_blank" href="/a002">example 2</a>.<br />
            Thank you.
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page='../../../partials/foot.jsp'/>

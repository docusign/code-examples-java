<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>5. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${clickwraps['clickwraps'] != null }">
        <p>Please choose a clickwrap</p>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="clickwrapId">Clickwrap</label>
                <select class="custom-select" id="clickwrapId" name="clickwrapId">
                    <c:forEach items="${clickwraps['clickwraps']}" var="clickwrap">
                        <option value="${clickwrap['clickwrapId']}">${clickwrap['clickwrapName']}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            Problem: You do not have clickwraps. Go to example#1 and create one: 
            <a href="c001">create clickwrap.</a> <br/>
        </p>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>
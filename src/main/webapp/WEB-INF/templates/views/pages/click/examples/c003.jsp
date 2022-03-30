<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>3. Create a new clickwrap version.</h4>

<p>
    Creates a new version of a clickwrap.
</p>
<p>
    You can specify whether you require users who have previously accepted the clickwrap 
    to accept the new version when they return to your website.
</p>
<p>API method used:
    <a target ="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/click-api/reference/accounts/clickwraps/createclickwrapversion/">ClickWraps:createClickwrapVersion</a>
</p>
<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<c:choose>
    <c:when test="${clickwraps['clickwraps'] != null }">
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="clickwrapId">Clickwrap</label>
                <select class="custom-select" id="clickwrapId" name="clickwrapId">
                    <c:forEach items="${clickwraps['clickwraps']}" var="clickwrap">
                        <option value="${clickwrap['clickwrapId']}">${clickwrap['clickwrapName']}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="clickwrapName">New Name</label>
                <input type="text" class="form-control" id="clickwrapName" name="clickwrapName" required>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            Problem: You do not have clickwraps to activate. Go to example#1 and create one: 
            <a href="c001">create clickwrap.</a> <br/>
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
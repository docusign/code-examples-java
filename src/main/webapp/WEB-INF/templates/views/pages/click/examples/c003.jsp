<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>3. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
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
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
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
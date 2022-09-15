<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>2. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${clickwraps['clickwraps'] != null }">
        <p>Please choose a clickwrap to activate</p>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="clickwrap">Clickwrap</label>
                <select class="custom-select" id="clickwrap" name="clickwrap">
                    <c:forEach items="${clickwraps['clickwraps']}" var="clickwrap">
                        <option value="${clickwrap['clickwrapId']}:${clickwrap['versionNumber']}">${clickwrap['clickwrapName']}</option>
                    </c:forEach>
                </select>
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
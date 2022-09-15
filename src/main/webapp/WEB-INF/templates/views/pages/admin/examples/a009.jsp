<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>


<h4>9. ${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
   ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${emailAddress != null }">
        <p>Delete user product permission profile for the following email: <b>${emailAddress}</b></p>

        <form class="eg" method="post" data-busy="form">
            <div class="form-group">
                <label for="Products">Choose which product permission profile you would like to delete</label>
                <select id="Products" name="ProductId" class="form-control">
                    <c:forEach var="entry" items="${listProducts}">
                        <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
                    </c:forEach>
                </select>
            </div>

            <input type="hidden" name="csrf_token" value="${csrfToken}"/>
            <button type="submit" class="btn btn-primary">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>
            Problem: You do not have the user to change permissions for. Go to example#2 and create one:
            <a href="a002">create active CLM/eSign User.</a> <br/>
        </p>
    </c:otherwise>
</c:choose>

<jsp:include page='../../../partials/foot.jsp'/>

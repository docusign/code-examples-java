<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>2. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<jsp:include page="../../links_to_api_methods.jsp" />

<c:choose>
    <c:when test="${templateList.size() == 0}">
        <p>Cannot find any templates. Please first <a target ='_blank' rel="noopener noreferrer" href="https://developers.docusign.com/rooms-api/guides/templates#creating-a-room-template">create a template</a>.
        </p>
    </c:when>
    <c:otherwise>
        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="roomName">Room Name</label>
                <input type="text" class="form-control" id="roomName" placeholder="Please write room name" name="roomName" required>
            </div>
            <div class="form-group">
                <label for="roomTemplate">Template</label>
                <select id="roomTemplate" name="roomTemplateId" class="form-control">
                    <c:forEach items="${templateList}" var="template">
                        <option value="${template.roomTemplateId}">${template.name}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">Submit</button>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
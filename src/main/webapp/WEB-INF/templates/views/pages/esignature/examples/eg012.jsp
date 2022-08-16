<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />

<p>
    View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>
<c:if test="${not envelopeOk}">
    <p><strong>Optional:</strong> to use the <em>Envelope's document view</em> please first create an envelope using
        <a href="eg002">example 2.</a></p>
</c:if>


<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="startingView">Starting View</label>
        <select id="startingView" name="startingView" class="form-control">
            <option value="frontPage" selected>Front page</option>
            <option value="envelope" ${envelopeOk ? '' : 'disabled="true"'} >
                The envelope's documents view
            </option>
        </select>
    </div>
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-docu">Continue</button>
</form>

<jsp:include page="../../../partials/foot.jsp"/>

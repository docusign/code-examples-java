<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>


<h4>5. ${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>


<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<jsp:include page="../../links_to_api_methods.jsp" />

<p>
   View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>
  
  <form class="eg" action="" method="post" data-busy="form">
      <input type="hidden" name="csrf_token" value="${csrfToken}">
      <button type="submit" class="btn btn-docu">Continue</button>
  </form>

<jsp:include page='../../../partials/foot.jsp'/>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>


<h4>5. Audit users</h4>
<p>
  Demonstrates how to audit the users in your account by retrieving the profiles of users that were modified after a specified date.
</p>


<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>API methods used:
  <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/admin-api/reference/usermanagement/users/getusers/">Users:getUsers</a>, 
  <a target="_blank" rel="noopener noreferrer" href="https://developers.docusign.com/docs/admin-api/reference/usermanagement/esignusermanagement/getuserprofiles/">eSignUserManagement:getUserProfiles</a>.
  </p>

<p>
   View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>
  
  <form class="eg" action="" method="post" data-busy="form">
      <input type="hidden" name="csrf_token" value="${csrfToken}">
      <button type="submit" class="btn btn-docu">Continue</button>
  </form>

<jsp:include page='../../../partials/foot.jsp'/>

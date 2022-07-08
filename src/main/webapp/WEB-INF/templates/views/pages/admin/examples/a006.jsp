<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>6. Retrieve the user's DocuSign profile using an email address</h4>
<p>Demonstrates how to obtain the user's DocuSign profile information across all DocuSign accounts by specifying the user's email address.</p>

<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>

<p>
    API method used:
    <a target="_blank" rel='noopener noreferrer'
       href="https://developers.docusign.com/docs/admin-api/reference/usermanagement/multiproductusermanagement/getuserdsprofilesbyemail/">MultiProductUserManagement:getUserDSProfilesByEmail</a>.
</p>

<p>
    View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
        <label for="email">Email address</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="example@example.com" required>
        <small id="accessHelp" class="form-text text-muted">We will never share your email with anyone else.</small>

        <br/>
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="btn btn-docu">Submit</button>
    </div>
</form>

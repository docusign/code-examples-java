<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescriptionExtended()}</p>

<c:if test="${showDoc}">
    <p><a target="_blank" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />

<p>
  View source file <a target="_blank" href="${sourceUrl}">${sourceFile}</a> on GitHub.
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
      <label for="signerEmail">Signer Email</label>
      <input type="email" class="form-control" id="signerEmail" name="signerEmail"
             aria-describedby="emailHelp" placeholder="pat@example.com" required
             value="${signerEmail}" />
      <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
    </div>
    <div class="form-group">
      <label for="signer_name">Signer Name</label>
      <input type="text" class="form-control" id="signerName" placeholder="Pat Johnson" name="signerName"
             value="${signerName}" required />
    </div>
    <div class="form-group">
      <label for="resumeDate">Resume Date</label>
      <input type="date" class="form-control" id="resumeDate" name="resumeDate" required />
      <small id="dateHelp" class="form-text text-muted">Please choose a date in the future.</small>
    </div>
    <input type="hidden" name="csrfToken" value="${csrfToken}"/>
    <button type="submit" class="btn btn-docu">Submit</button>
  </form>
</br>
</br>
</br>

<jsp:include page="../../../partials/foot.jsp"/>

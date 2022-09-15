<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>

<c:if test="${showDoc}">
    <p><a target="_blank" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />

<p>
  ${viewSourceFile}
</p>

<form class="eg" action="" method="post" data-busy="form">
    <div class="form-group">
      <label for="signerEmail">First Signer Email</label>
      <input type="email" class="form-control" id="signerEmail" name="signerEmail"
             aria-describedby="emailHelp" placeholder="pat@example.com" required
             value="${signerEmail}" />
      <small id="emailHelp" class="form-text text-muted">${launcherTexts.getHelpingTexts().getEmailWontBeShared()}</small>
    </div>
    <div class="form-group">
      <label for="signerName">First Signer Name</label>
      <input type="text" class="form-control" id="signerName" placeholder="Pat Johnson" name="signerName"
             value="${signerName}" required />
    </div>
    <div class="form-group">
      <label for="signerEmail2">Second Signer Email</label>
      <input type="email" class="form-control" id="signerEmail2" name="signerEmail2"
             aria-describedby="emailHelp" placeholder="pat@example.com" required />
      <small id="emailHelp" class="form-text text-muted">${launcherTexts.getHelpingTexts().getCCEmailShouldDifferFromSigner()}}</small>
    </div>
    <div class="form-group">
      <label for="signerName2">Second Signer Name</label>
      <input type="text" class="form-control" id="signerName2" placeholder="Pat Johnson" name="signerName2"
             required />
    </div>
    <div class="form-group">
      <label for="delay">Delay (in hours)</label>
      <input type="number" class="form-control" id="delay" name="delay" required />
    </div>
    <input type="hidden" name="csrfToken" value="${csrfToken}"/>
    <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
  </form>
</br>
</br>
</br>


<jsp:include page="../../../partials/foot.jsp"/>

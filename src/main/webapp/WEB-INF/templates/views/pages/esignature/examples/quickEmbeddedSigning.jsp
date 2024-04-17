<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="signerEmailInputNumber" value="0" scope="page" />
<c:set var="signerNameInputNumber" value="1" scope="page" />

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="assets/favicon.png">

    <title>${title}</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/default.min.css"> -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/darcula.min.css">
    <!-- Custom styles for this template -->
    <link href="/assets/css.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand"  target="_blank" href="https://developers.docusign.com">Docusign Developer</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto"></ul>
        <c:if test="${locals.user != null}">
           <span class="navbar-text">
               ${launcherTexts.getWelcomeText()
                       .replaceFirst("\\{0}", locals.user.name)
                       .replaceFirst("\\{1}", locals.session.accountName)}
          </span>
        </c:if>
    </div>
</nav>

<div class="container">

    <h4>${example.getExampleName()}</h4>
    <p>${example.getExampleDescription()}</p>
    <c:if test="${showDoc}">
        <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
    </c:if>

    <p>
        <c:choose>
          <c:when test="${example.getLinksToAPIMethod().size() == 1}">
            <span>${launcherTexts.getAPIMethodUsed()}</span>
          </c:when>
          <c:otherwise>
            <span>${launcherTexts.getAPIMethodUsedPlural()}</span>
          </c:otherwise>
        </c:choose>
      
        <c:forEach var="link" items="${example.getLinksToAPIMethod()}">
          <a href="${link.getPath()}">${link.getPathName()}</a>
      
          <c:choose>
            <c:when test="${example.getLinksToAPIMethod().size() == example.getLinksToAPIMethod().indexOf(link) + 1}">
              <span>.</span>
            </c:when>
            <c:when test="${example.getLinksToAPIMethod().size() - 1 == example.getLinksToAPIMethod().indexOf(link) + 1}">
              <span>and</span>
            </c:when>
            <c:otherwise>
              <span>,</span>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </p>
      
    <p>
        ${viewSourceFile}
    </p>

    <form class="eg" action="" method="post" data-busy="form">
        <div class="form-group">
            <label for="signerEmail">
                ${example.getForms().get(formNumber).getInputs().get(signerEmailInputNumber).getInputName()}
            </label>

            <input type="email"
                   class="form-control"
                   id="signerEmail"
                   name="signerEmail"
                   aria-describedby="emailHelp"
                   placeholder="${example.getForms().get(formNumber).getInputs().get(signerEmailInputNumber).getInputPlaceholder()}"
                   required
                   value="${locals.dsConfig.signerEmail}">

            <small id="emailHelp" class="form-text text-muted">
                ${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
            </small>
        </div>
        <div class="form-group">
            <label for="signerName">
                ${example.getForms().get(formNumber).getInputs().get(signerNameInputNumber).getInputName()}
            </label>

            <input type="text"
                   class="form-control"
                   id="signerName"
                   placeholder="${example.getForms().get(formNumber).getInputs().get(signerNameInputNumber).getInputPlaceholder()}"
                   name="signerName"
                   value="${locals.dsConfig.signerName}"
                   required>
        </div>
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
    </form>
</div>
</body>
</html>

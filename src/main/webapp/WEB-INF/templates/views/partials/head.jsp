<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/assets/favicon.png">

    <title>${title}</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <!-- Custom styles for this template -->
    <link href="/assets/css.css" rel="stylesheet">
</head>

<body>
<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand"  target="_blank" href="https://developers.docusign.com">Docusign Developer</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault"
            aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/">${launcherTexts.getHomeButton()} <span class="sr-only">(current)</span></a>
            </li>
            <c:choose>
                <c:when test="${locals.user != null}">
                    <li>
                        <a class="nav-link" href="/logout" id="logout"
                           data-busy="href">${launcherTexts.getLogoutButton()} <span class="sr-only">(current)</span></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a class="nav-link" href="/ds/mustAuthenticate" id="login"
                           data-busy="href">${launcherTexts.getLoginButton()} <span class="sr-only">(current)</span></a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
        <c:if test="${locals.user != null}">
           <span class="navbar-text">
               ${launcherTexts.getWelcomeText()
                       .replaceFirst("\\{0}", locals.user.name)
                       .replaceFirst("\\{1}", locals.session.accountName)}
          </span>
        </c:if>
    </div>
</nav>
    <div class="col-md-12 feedback" id="feedback">
        <h3>Working...&nbsp;&nbsp;&nbsp;<span></span></h3>
    </div>

    <section id="busy" class="feedback">
        <div class="spinner">
            <div class="rect1"></div>
            <div class="rect2"></div>
            <div class="rect3"></div>
            <div class="rect4"></div>
            <div class="rect5"></div>
        </div>
    </section>

    <p id="download-continue" class="feedback"><a href="/">${launcherTexts.getContinueButton()}</a></p>

    <div class="container-full-bg">
    <section id="content">
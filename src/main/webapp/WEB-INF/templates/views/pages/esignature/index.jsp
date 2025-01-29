<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <jsp:include page="../../partials/head.jsp" />

  <div class="jumbotron jumbotron-fluid">
    <table>
      </br>
      <tbody>
        <tr>
          <td>
            <h1 class="display-4">Java Launcher</h1>
            <p class="Xlead">${launcherTexts.getHomePageText()}</p>
          </td>
          <td>
          </td>
        </tr>
      </tbody>
    </table>
  </div>


  <div class="container" style="margin-top: 40px" id="index-page">
    <c:if test="${showDoc == true}">
      <p><a target='_blank' href='${documentation}'>Documentation</a> on using OAuth Authorization Code Grant from a
        Java application.</p>
    </c:if>

    <div class="form-group has-search">
      <span class="fa fa-search form-control-feedback"></span>
      <input type="text" class="form-control" id="code_example_search" placeholder="Search for code example">
    </div>

    <div id="api_json_data" class="hidden"></div>
    <div id="cfr11_data" class="hidden">${statusCFR}</div>

    <div id="filtered_code_examples" class="container" style="margin-top: 10px; padding-left: 0px;">
      <c:forEach var="apis" items="${codeExampleGroups}">

        <c:choose>
          <c:when test="${apis.getName().equals('eSignature')}">
            <c:set var="linkToCodeExample" value="eg" scope="page" />
          </c:when>
          <c:when test="${apis.getName().equals('Click')}">
            <c:set var="linkToCodeExample" value="c" scope="page" />
          </c:when>
          <c:when test="${apis.getName().equals('Monitor')}">
            <c:set var="linkToCodeExample" value="m" scope="page" />
          </c:when>
          <c:when test="${apis.getName().equals('Rooms')}">
            <c:set var="linkToCodeExample" value="r" scope="page" />
          </c:when>
          <c:when test="${apis.getName().equals('Connect')}">
            <c:set var="linkToCodeExample" value="con" scope="page" />
          </c:when>
          <c:when test="${apis.getName().equals('WebForms')}">
            <c:set var="linkToCodeExample" value="web" scope="page" />
          </c:when>
          <c:when test="${apis.getName().equals('Notary')}">
            <c:set var="linkToCodeExample" value="n" scope="page" />
          </c:when>
          <c:otherwise>
            <c:set var="linkToCodeExample" value="a" scope="page" />
          </c:otherwise>
        </c:choose>

        <c:forEach var="group" items="${apis.getGroups()}">
          <h2>${group.getName()}</h2>

          <c:forEach var="example" items="${group.getExamples()}">
            <c:if
              test="${example.getSkipForLanguages() == null || !example.getSkipForLanguages().toLowerCase().contains('java')}">
              <c:if test="${!apis.getName().equals('eSignature') ||
          ((example.getCFREnabled() == 'AllAccounts') ||
          (statusCFR == 'enabled' && example.getCFREnabled() == 'CFROnly') ||
          (statusCFR != 'enabled' && example.getCFREnabled() == 'NonCFR'))}">
                <h4 id="${String.format('example%03d', example.getExampleNumber())}">
                  <a href="${String.format('%s%03d', linkToCodeExample, example.getExampleNumber())}">
                    ${example.getExampleName()}
                  </a>
                </h4>

                <p>${example.getExampleDescription()}</p>

                <p>
                  <c:if test="${example.getLinksToAPIMethod().size() != 0}">
                    <c:choose>
                      <c:when test="${example.getLinksToAPIMethod().size() == 1}">
                        <span>${launcherTexts.getAPIMethodUsed()}</span>
                      </c:when>
                      <c:otherwise>
                        <span>${launcherTexts.getAPIMethodUsedPlural()}</span>
                      </c:otherwise>
                    </c:choose>

                    <c:forEach var="link" items="${example.getLinksToAPIMethod()}">
                      <a href="${link.getPath()}">
                        ${link.getPathName()}
                      </a>

                      <c:choose>
                        <c:when
                          test="${example.getLinksToAPIMethod().size() == example.getLinksToAPIMethod().indexOf(link) + 1}">
                          <span>.</span>
                        </c:when>
                        <c:when
                          test="${example.getLinksToAPIMethod().size() - 1 == example.getLinksToAPIMethod().indexOf(link) + 1}">
                          <span>and</span>
                        </c:when>
                        <c:otherwise>
                          <span>,</span>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </c:if>
                </p>
              </c:if>
            </c:if>
          </c:forEach>
        </c:forEach>
      </c:forEach>
    </div>

  </div>

  <!-- anchor-js is only for the index page -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
  <script>anchors.options.placement = 'left'; anchors.add('h4')</script>
  <script>
    let apiData = ${ APIData };
    document.getElementById("api_json_data").innerText = JSON.stringify(apiData);
  </script>
  <jsp:include page="../../partials/foot.jsp" />
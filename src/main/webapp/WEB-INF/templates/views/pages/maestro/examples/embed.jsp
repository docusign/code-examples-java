<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<p>${example.getResultsPageText()}</p>


<iframe width="900" height="600" src="${url}">
</iframe>

<p><a href="/">${launcherTexts.getContinueButton()}</a></p>

<jsp:include page="../../../partials/foot.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>
<p>${example.getResultsPageText()}</p>


<!--
//ds-snippet-start:Maestro1Step6
-->
<iframe width="900" height="600" src="${url}">
</iframe>
<!--
//ds-snippet-end:Maestro1Step6
-->

<p><a href="/">${launcherTexts.getContinueButton()}</a></p>

<jsp:include page="../../../partials/foot.jsp"/>

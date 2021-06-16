<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<jsp:include page='../../../partials/head.jsp'/>

<h4>1. Get monitoring data</h4>
<p>Demonstrates how to get and display all of your organization's monitoring data.</p>
<c:if test='${showDoc}'>
    <p><a target='_blank' rel="noopener noreferrer" href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<p>API method used:
    <a target='_blank' rel='noopener noreferrer'
       href='https://developers.docusign.com/docs/monitor-api/reference/monitor/dataset/getstream/'>DataSet:getStream</a>.
</p>
<p>
    View source file <a target='_blank' href='${sourceUrl}'>${sourceFile}</a> on GitHub.
</p>

<form class='eg' action='' method='post' data-busy='form'>
    <input type='hidden' name='_csrf' value='${csrfToken}'>
    <button type='submit' class='btn btn-docu'>Continue</button>
</form>

<jsp:include page='../../../partials/foot.jsp'/>

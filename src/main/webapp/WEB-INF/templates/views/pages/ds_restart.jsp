<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<br>
<br>
<p>  Please restart the app to finish switching to a new API.</p>
<p>
    Quit the current app with <code>Control + C</code>, then run
    <code>mvn package</code> followed by 
    <code>java -Dspring.profiles.active=dev -jar target/code-examples-java-1.0-SNAPSHOT.war</code>
    again or rerun it using the <code>Run button</code> in IntelliJ Ultimate or Eclipse.
</p>

<jsp:include page="../partials/foot.jsp"/>
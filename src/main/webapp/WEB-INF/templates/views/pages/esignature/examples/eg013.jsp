<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<h4>${example.getExampleName()}</h4>

<c:if test="${templateOk}">
    <p>${example.getExampleDescription()}</p>
</c:if>

<c:if test="${showDoc}">
    <p><a target='_blank' href='${documentation}'>Documentation</a> about this example.</p>
</c:if>


<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>
<c:choose>
    <c:when test="${templateOk}">
        <p>The template you created via Create a template will be used.</p>

        <form class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="signerEmail">Signer Email</label>
                <input type="email" class="form-control" id="signerEmail" name="signerEmail"
                       aria-describedby="emailHelp" placeholder="pat@example.com" required
                       value="${locals.dsConfig.signerEmail}">
                <small id="emailHelp" class="form-text text-muted">${launcherTexts.getHelpingTexts().getEmailWontBeShared()}
                </small>
            </div>
            <div class="form-group">
                <label for="signerName">Signer Name</label>
                <input type="text" class="form-control" id="signerName" placeholder="Pat Johnson" name="signerName"
                       value="${locals.dsConfig.signerName}" required>
            </div>
            <div class="form-group">
                <label for="ccEmail">CC Email</label>
                <input type="email" class="form-control" id="ccEmail" name="ccEmail"
                       aria-describedby="emailHelp" placeholder="pat@example.com" required>
                <small id="emailHelp" class="form-text text-muted">The email and/or name for the cc recipient must be
                    different
                    from the signer.
                </small>
            </div>
            <div class="form-group">
                <label for="ccName">CC Name</label>
                <input type="text" class="form-control" id="ccName" placeholder="Pat Johnson" name="ccName"
                       required>
            </div>
            <p>
            <hr class='styled'/>
            </p>
            <div class="form-group">
                <label for="item">Item</label>
                <select id="item" name="item" class="form-control">
                    <option>Apples</option>
                    <option selected>Avocados</option>
                    <option>Oranges</option>
                </select>
            </div>
            <div class="form-group">
                <label for="quantity">Quantity</label>
                <select id="quantity" name="quantity" class="form-control">
                    <option>10</option>
                    <option selected>20</option>
                    <option>30</option>
                    <option>40</option>
                    <option>50</option>
                    <option>60</option>
                    <option>70</option>
                    <option>80</option>
                    <option>90</option>
                    <option>100</option>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Problem: please first create the template using <a href="eg008">Create a template.</a> <br/>
            Thank you.</p>

        <form class="eg" action="eg008" method="get">
            <button type="submit" class="btn btn-docu">${launcherTexts.getContinueButton()}</button>
        </form>
    </c:otherwise>
</c:choose>


<jsp:include page="../../../partials/foot.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<c:set var="formNumber" value="0" scope="page" />
<c:set var="documentNameInputNumber" value="0" scope="page" />
<c:set var="documentPathInputNumber" value="1" scope="page" />
<c:set var="redirectToFirstCodeExampleNumber" value="0" scope="page" />
<c:set var="redirectToFirstCodeExample" value="href='work001'" scope="page" />

<h4>${example.getExampleName()}</h4>
<p>${example.getExampleDescription()}</p>
<jsp:include page="../../links_to_api_methods.jsp" />
<p>
    ${viewSourceFile}
</p>

<c:choose>
    <c:when test="${!workspaceOk}">
        <p>
            ${example.getRedirectsToOtherCodeExamples().get(redirectToFirstCodeExampleNumber).getRedirectText().replaceFirst("\\{0}", redirectToFirstCodeExample)}
        </p>
    </c:when>
    <c:otherwise>
        <form id="uploadForm" class="eg" action="" method="post" data-busy="form">
            <div class="form-group">
                <label for="documentName">
                    ${example.getForms().get(formNumber).getInputs().get(documentNameInputNumber).getInputName()}
                </label>

                <input type="text"
                       class="form-control"
                       id="documentName"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(documentNameInputNumber).getInputPlaceholder()}"
                       name="documentName"
                       required />

                <small id="nameError" class="form-text text-muted validation-error"></small>
                <small id="documentNameHelp" class="form-text text-muted">
                    ${launcherTexts.getHelpingTexts().getSpecifyNameWithExtension()}
                </small>
            </div>
            <div class="form-group">
                <label for="documentPath">
                    ${example.getForms().get(formNumber).getInputs().get(documentPathInputNumber).getInputName()}
                </label>
            
                <input type="file"
                       accept=".pdf"
                       class="form-control"
                       id="documentPath"
                       placeholder="${example.getForms().get(formNumber).getInputs().get(documentPathInputNumber).getInputPlaceholder()}"
                       name="documentPath"
                       required />
                       
                <small id="pathError" class="form-text text-muted validation-error"></small>
                <small id="documentNameHelp" class="form-text text-muted">
                    ${documentFolder}
                </small>
            </div>
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="btn btn-docu">${launcherTexts.getSubmitButton()}</button>
        </form>

        <script>
            const form = document.getElementById('uploadForm');
            form.addEventListener('submit', function (event) {
                const docNameInput = document.getElementById('documentName');
                const fileInput = document.getElementById('documentPath');
                const nameErrorEl = document.getElementById('nameError');
                const pathErrorEl = document.getElementById('pathError');
                
                const documentName = docNameInput.value.trim();
                const file = fileInput.files[0];
                
                // Validate documentName ends with .pdf
                if (!documentName.toLowerCase().endsWith(".pdf")) {
                    nameErrorEl.textContent = 'Document name must contain .pdf extension';
                    event.preventDefault();
                    event.stopImmediatePropagation();
                } else {
                    nameErrorEl.textContent = ''; // clear any previous errors
                }

                // Check extension
                const isPdf = file.name.toLowerCase().endsWith('.pdf');
                const isPdfMime = file.type === 'application/pdf';

                if (!isPdf || !isPdfMime) {
                    fileInput.value = ''; // reset file input
                    pathErrorEl.textContent = 'Invalid file type. Please upload a PDF file.';
                    event.preventDefault();
                    event.stopImmediatePropagation();
                } else {
                    pathErrorEl.textContent = ''; // clear any previous errors
                }
            }, true);
        </script>
    </c:otherwise>
</c:choose>

<jsp:include page="../../../partials/foot.jsp"/>
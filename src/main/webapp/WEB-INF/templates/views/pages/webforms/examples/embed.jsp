<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../../partials/head.jsp"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
</head>

<body>
<div id="app">
    <div id="webform-customer-app-area">
        <h5 id="webforms-heading">The web form has been embedded below using the DocuSign.js library.</h5>
        <div id="docusign" class="webform-iframe-container">
            <p>Web Form will render here</p>
        </div>
    </div>
</div>
</body>
</html>

<p><a href="/">${launcherTexts.getContinueButton()}</a></p>

<!--
//ds-snippet-start:WebForms1Step6
-->


<script src='https://js.docusign.com/bundle.js'></script>

<script>
    async function loadWebform() {
        var instanceToken = '${instanceToken}'.replace(/&#x2B;/g, "+");
        const { loadDocuSign } = window.DocuSign
        const docusign = await loadDocuSign('${integrationKey}');

        const webFormOptions = {
            //Used with the runtime API workflow, for private webforms this is needed to render anything
            instanceToken: instanceToken,
            //Controls whether the progress bar is shown or not
            hideProgressBar: false,
            //Right now this parameter doesn't do anything but in the future
            //it will be used to customize visual elements of the form experience
            //such as the font color and other things
            styles: {
                fontColor: "black",
            },
            //These styles get passed directly to the iframe that is rendered
            iframeStyles: {
                minHeight: "1500px",
            },
            //Controls the auto resize behavior of the iframe
            autoResizeHeight: true,
            //These values are passed to the iframe URL as query params
            tracking: {
                "tracking-field": "tracking-value",
            },
            //These values are passed to the iframe URL as hash params
            hidden: {
                "hidden-field": "hidden-value",
            },
        };

        const webFormWidget = docusign.webforms({
            url: '${url}',
            options: webFormOptions,
        })

        webFormWidget.mount("#docusign");
    }

    loadWebform();
</script>

<!--
//ds-snippet-end:WebForms1Step6
-->


<jsp:include page="../../../partials/foot.jsp"/>

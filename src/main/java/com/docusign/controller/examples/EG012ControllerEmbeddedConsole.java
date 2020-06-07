package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ConsoleViewRequest;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import com.docusign.esign.model.ViewUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/eg012")
public class EG012ControllerEmbeddedConsole extends EGController {

    @Autowired
    private HttpSession session;

    @Override
    protected void addSpecialAttributes(ModelMap model) {
        model.addAttribute("envelopeOk", session.getAttribute("envelopeId") != null);
    }

    @Override
    protected String getEgName() {
        return "eg012";
    }

    @Override
    protected String getTitle() {
        return "Embedded DocuSign web tool";
    }

    @Override
    protected String getResponseTitle() {
        return null;
    }

    @Override
    // ***DS.snippet.0.start
    protected EnvelopeDocumentsResult doWork(WorkArguments args, ModelMap model,
                                             String accessToken, String basePath) throws ApiException {
        // Data for this method
        // accessToken    (argument)
        // basePath       (argument)
        // config.appUrl  (url of the application itself)

        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 1. create the NDSE view
        args.setDsReturnUrl(config.appUrl + "/ds-return");
        ConsoleViewRequest viewRequest = makeConsoleViewRequest(args);

        // Step 2. Call the CreateSenderView API
        // Exceptions will be caught by the calling function
        ViewUrl results = envelopesApi.createConsoleView(args.getAccountId(), viewRequest);
        // process results
        args.setRedirectUrl("redirect:" + results.getUrl());
        System.out.println("NDSE view URL: " + results.getUrl());

        return null;
    }

    private ConsoleViewRequest makeConsoleViewRequest(WorkArguments args) {
        // Data for this method
        String returnUrl = args.getDsReturnUrl();
        String startingView = args.getStartingView();
        String envelopeId = args.getEnvelopeId();


        ConsoleViewRequest viewRequest = new ConsoleViewRequest();
        // Set the url where you want the recipient to go once they are done
        // with the NDSE. It is usually the case that the
        // user will never "finish" with the NDSE.
        // Assume that control will not be passed back to your app.
        viewRequest.setReturnUrl(returnUrl);

        if ("envelope".equalsIgnoreCase(startingView) && envelopeId != null) {
            viewRequest.setEnvelopeId(envelopeId);
        }

        return viewRequest;
    }
    // ***DS.snippet.0.end
}

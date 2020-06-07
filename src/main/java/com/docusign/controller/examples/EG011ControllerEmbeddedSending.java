package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ReturnUrlRequest;
import com.docusign.esign.model.ViewUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;

@Controller
@RequestMapping("/eg011")
public class EG011ControllerEmbeddedSending extends EGController {
    @Override
    protected void addSpecialAttributes(ModelMap model) {

    }

    @Override
    protected String getEgName() {
        return "eg011";
    }

    @Override
    protected String getTitle() {
        return "Signing request by email";
    }

    @Override
    protected String getResponseTitle() {
        return null;
    }

    @Autowired
    EG002ControllerSigningViaEmail controller2;

    @Override
    // ***DS.snippet.0.start
    protected EnvelopeDocumentsResult doWork(WorkArguments args, ModelMap model,
                                             String accessToken, String basePath) throws ApiException, IOException {
        // Data for this method
        // accessToken    (argument)
        // basePath       (argument)
        // config.appUrl  (url of the application itself)
        String startingView = args.getStartingView();
        String accountId = args.getAccountId();


        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 1. Make the envelope with "created" (draft) status
        args.setStatus("created");
        EnvelopeSummary results = (EnvelopeSummary) controller2.doWork(args, model, accessToken, basePath);
        String envelopeId = results.getEnvelopeId();

        // Step 2. create the sender view
        // Call the CreateSenderView API
        // Exceptions will be caught by the calling function
        //
        // Prepare the request
        String returnUrl = config.appUrl + "/ds-return";
        ReturnUrlRequest viewRequest = new ReturnUrlRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        viewRequest.setReturnUrl(returnUrl);
        // Call the API
        ViewUrl result1 = envelopesApi.createSenderView(accountId, envelopeId, viewRequest);

        // Process result
        // Switch to Recipient and Documents view if requested by the user
        String url = result1.getUrl();
        System.out.println("startingView: " + startingView);
        if ("recipient".equalsIgnoreCase(startingView)) {
            url = url.replace("send=1", "send=0");
        }

        System.out.println("Sender view URL: " + url);
        args.setRedirectUrl("redirect:" + url);
        return null;
    }
    // ***DS.snippet.0.end
}

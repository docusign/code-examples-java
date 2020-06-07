package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.EnvelopesApi.ListStatusChangesOptions;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopesInformation;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eg003")
public class EG003ControllerListEnvelopes extends EGController {
    @Override
    protected void addSpecialAttributes(ModelMap model) {
    }

    @Override
    protected String getEgName() {
        return "eg003";
    }

    @Override
    protected String getTitle() {
        return "List envelopes";
    }

    @Override
    protected String getResponseTitle() {
        return "List envelopes results";
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            String accessToken, String basePath) throws ApiException {
        // Data for this method
        // accessToken    (argument)
        // basePath       (argument)
        String accountId = args.getAccountId();

        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();

        LocalDate date = LocalDate.now().minusDays(30);
        options.setFromDate(date.toString("yyyy/MM/dd"));

        EnvelopesInformation results = envelopesApi.listStatusChanges(accountId, options);
        setMessage("Results from the Envelopes::listStatusChanges method:");
        return results;
    }
    // ***DS.snippet.0.end
}

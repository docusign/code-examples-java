package com.docusign.controller.navigator.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.navigator.services.NavigatorMethodsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to bulk upload documents to Navigator.
 * Step 1: Create a bulk upload job.
 * Step 2: Upload demo documents to the job's blob storage URLs.
 * Step 3: Mark the job as complete.
 */
@Controller
@RequestMapping("/nav003")
public class Nav003BulkUploadDocumentsController extends AbstractNavigatorController {

    private static final String UPLOAD_DOCUMENTS_PAGE =
        "pages/navigator/examples/nav003UploadDocuments";

    private static final String COMPLETE_UPLOAD_PAGE =
        "pages/navigator/examples/nav003CompleteUpload";

    public Nav003BulkUploadDocumentsController(DSConfiguration config, Session session, User user) {
        super(config, "nav003", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws Exception {
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();

        var jobInfo = NavigatorMethodsService.createBulkUploadJob(accountId, accessToken);
        session.setBulkJobId(jobInfo.getJobId());
        session.setBulkUploadUrls(jobInfo.getUploadUrls());

        return "redirect:/nav003/uploadDocuments";
    }

    @GetMapping("/uploadDocuments")
    public String getUploadDocuments(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        return UPLOAD_DOCUMENTS_PAGE;
    }

    @PostMapping("/uploadDocuments")
    public String postUploadDocuments(WorkArguments args, ModelMap model) throws Exception {
        if (session.getBulkUploadUrls() == null) {
            return "redirect:/nav003";
        }
        NavigatorMethodsService.uploadDocumentsToJob(session.getBulkUploadUrls());
        return "redirect:/nav003/completeUpload";
    }

    @GetMapping("/completeUpload")
    public String getCompleteUpload(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        return COMPLETE_UPLOAD_PAGE;
    }

    @PostMapping("/completeUpload")
    public String postCompleteUpload(WorkArguments args, ModelMap model) throws Exception {
        var jobId = session.getBulkJobId();
        if (jobId == null) {
            return "redirect:/nav003";
        }
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();

        NavigatorMethodsService.completeBulkUploadJob(accountId, accessToken, jobId);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().AdditionalPage.get(1).ResultsPageText)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}

package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.DeleteRestoreEnvelopeService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.Folder;
import com.docusign.esign.model.FoldersResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;


/**
 * Used to delete the envelope and restore it back.
 */
@Controller
@RequestMapping("/eg045")
public class EG045ControllerDeleteRestoreEnvelope extends AbstractEsignatureController {
    public static final String RECYCLE_BIN_FOLDER_ID = "recyclebin";

    public static final String SENT_ITEMS_FOLDER_NAME = "Sent items";

    public static final String EXAMPLE_NUMBER = "/eg045";

    public static final String RESTORE_ENVELOPE = "/restoreEnvelope";

    public static final String RESTORE_ENVELOPE_PAGE = "pages/esignature/examples/eg045RestoreEnvelope";

    public EG045ControllerDeleteRestoreEnvelope(DSConfiguration config, Session session, User user){
        super(config, "eg045", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute("envelopeId", session.getEnvelopeId());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws Exception {
        String envelopeId = args.getEnvelopeId();
        session.setEnvelopeId(envelopeId);

        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        DeleteRestoreEnvelopeService.moveEnvelopeToFolder(
                apiClient,
                session.getAccountId(),
                envelopeId,
                RECYCLE_BIN_FOLDER_ID,
                null);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().AdditionalPage.get(0).ResultsPageText)
                .withRedirect(EXAMPLE_NUMBER + RESTORE_ENVELOPE)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }

    @GetMapping(value = RESTORE_ENVELOPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRestoreEnvelope(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        String envelopeId = session.getEnvelopeId();
        String restoreEnvelopeText = MessageFormat.format(
                config.getCodeExamplesText().SupportingTexts.HelpingTexts.EnvelopeWillBeRestored,
                envelopeId
        );

        model.addAttribute("restoreText", restoreEnvelopeText);
        model.addAttribute("envelopeId", envelopeId);

        return RESTORE_ENVELOPE_PAGE;
    }

    @PostMapping(value = RESTORE_ENVELOPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String postRestoreEnvelope(WorkArguments args, ModelMap model) throws Exception {
        String envelopeId = session.getEnvelopeId();
        String accountId = session.getAccountId();
        String folderName = args.getFolderName() != null ? args.getFolderName() : SENT_ITEMS_FOLDER_NAME;

        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        FoldersResponse availableFolders = DeleteRestoreEnvelopeService.getFolders(apiClient, accountId);
        Folder folder = availableFolders.getFolders().stream()
                .filter(f -> f.getName().equals(folderName))
                .findFirst()
                .orElse(null);

        if(folder == null) {
            DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                    .withMessage(getTextForCodeExampleByApiType().AdditionalPage.get(1).ResultsPageText)
                    .withRedirect(EXAMPLE_NUMBER + RESTORE_ENVELOPE)
                    .addToModel(model, config);

            return DONE_EXAMPLE_PAGE;
        }

        DeleteRestoreEnvelopeService.moveEnvelopeToFolder(
                apiClient,
                accountId,
                envelopeId,
                folder.getFolderId(),
                RECYCLE_BIN_FOLDER_ID);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(
                        MessageFormat.format(
                                getTextForCodeExampleByApiType().ResultsPageText,
                                folder.getType(),
                                folderName
                        )
                )
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}

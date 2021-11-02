package com.services.click;

import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.DisplaySettings;
import com.docusign.click.model.Document;
import com.docusign.controller.click.examples.ClickwrapHelper;

import java.io.IOException;

public final class CreateClickwrapService {
    public static ClickwrapRequest createClickwrapRequest(
            String clickwrapName,
            String fileName,
            String documentName,
            Integer documentOrder
    ) throws IOException {
        Document document = ClickwrapHelper.createDocumentFromFile(fileName, documentName, documentOrder);
        DisplaySettings displaySettings = new DisplaySettings()
                .displayName("Terms of Service")
                .consentButtonText("I Agree")
                .downloadable(true)
                .format("modal")
                .mustRead(true)
                .mustView(true)
                .requireAccept(true)
                .documentDisplay("document");

        return new ClickwrapRequest()
                .addDocumentsItem(document)
                .clickwrapName(clickwrapName)
                .requireReacceptance(true)
                .displaySettings(displaySettings);
    }
}

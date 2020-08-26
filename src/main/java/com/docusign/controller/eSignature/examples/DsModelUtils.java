package com.docusign.controller.eSignature.examples;

import com.docusign.esign.model.AccountRoleSettings;
import com.docusign.esign.model.SettingsMetadata;


/**
 * Helper functions to create and modify objects provided by the DocuSign API.
 */
public final class DsModelUtils {

    public static final String FALSE = "false";
    public static final String TRUE = "true";

    private DsModelUtils() {}

    /**
     * Creates {@link AccountRoleSettings} with default values. Now eSignature
     * API has an error. It requires a member 'signingUiVersion' but this class
     * doesn't contain such member.
     * @return a new AccountRoleSettings object
     */
    public static AccountRoleSettings createDefaultRoleSettings() {
        return new AccountRoleSettings()
                .allowAccountManagement(TRUE)
                .useNewDocuSignExperienceInterface("optional")
                .canCreateWorkspaces(TRUE)
                .allowBulkSending(TRUE)
                .allowEnvelopeSending(TRUE)
                .allowESealRecipients(FALSE)
                .allowSignerAttachments(TRUE)
                .allowTaggingInSendAndCorrect(TRUE)
                .allowWetSigningOverride(TRUE)
                .allowedAddressBookAccess("personalAndShared")
                .allowedClickwrapsAccess("create")
                .allowedTemplateAccess("share")
                .enableRecipientViewingNotifications(TRUE)
                .enableSequentialSigningInterface(TRUE)
                .receiveCompletedSelfSignedDocumentsAsEmailLinks(FALSE)
                .useNewSendingInterface(TRUE)
                .allowSupplementalDocuments(TRUE)
                .supplementalDocumentsMustView(TRUE)
                .supplementalDocumentsMustAccept(TRUE)
                .supplementalDocumentsMustRead(TRUE)
                .disableDocumentUpload(FALSE)
                .disableOtherActions(FALSE)
                .allowAutoTagging(TRUE)
                .allowApiAccess(TRUE)
                .allowApiAccessToAccount(TRUE)
                .allowApiSendingOnBehalfOfOthers(FALSE)
                .allowApiSequentialSigning(FALSE)
                .enableApiRequestLogging(TRUE)
                .allowDocuSignDesktopClient(FALSE)
                .allowSendersToSetRecipientEmailLanguage(TRUE)
                .allowVaulting(FALSE)
                .allowedToBeEnvelopeTransferRecipient(TRUE)
                .enableTransactionPointIntegration(FALSE)
                .powerFormRole("admin")
                .allowPowerFormsAdminToAccessAllPowerFormEnvelopes(FALSE)
                .vaultingMode("none")
                .signingUiVersionMetadata(new SettingsMetadata());
    }
}

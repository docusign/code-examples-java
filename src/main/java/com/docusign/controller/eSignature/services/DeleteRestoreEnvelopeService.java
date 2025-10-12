package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.FoldersApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.Folder;
import com.docusign.esign.model.FoldersRequest;
import com.docusign.esign.model.FoldersResponse;

import java.util.Collections;
import java.util.List;

public final class DeleteRestoreEnvelopeService {
    public static final String RECYCLE_BIN_FOLDER_ID = "recyclebin";

    public static FoldersResponse deleteEnvelope(
            ApiClient apiClient,
            String accountId,
            String envelopeId) throws Exception {
        FoldersApi foldersApi = new FoldersApi(apiClient);
        //ds-snippet-start:eSign45Step3
        FoldersRequest foldersRequest = new FoldersRequest();
        foldersRequest.setEnvelopeIds(Collections.singletonList(envelopeId));
        //ds-snippet-end:eSign45Step3
        //ds-snippet-start:eSign45Step4
        return foldersApi.moveEnvelopes(accountId, RECYCLE_BIN_FOLDER_ID, foldersRequest);
        //ds-snippet-end:eSign45Step4
    }
    //ds-snippet-start:eSign45Step6

    public static FoldersResponse moveEnvelopeToFolder(
            ApiClient apiClient,
            String accountId,
            String envelopeId,
            String folderId,
            String fromFolderId) throws Exception {
        FoldersApi foldersApi = new FoldersApi(apiClient);

        FoldersRequest foldersRequest = new FoldersRequest();
        foldersRequest.setFromFolderId(fromFolderId);
        foldersRequest.setEnvelopeIds(Collections.singletonList(envelopeId));

        return foldersApi.moveEnvelopes(accountId, folderId, foldersRequest);
    }
    //ds-snippet-end:eSign45Step6

     //ds-snippet-start:eSign45Step5
     
    public static FoldersResponse getFolders(
            ApiClient apiClient,
            String accountId) throws Exception {
        FoldersApi foldersApi = new FoldersApi(apiClient);
        return foldersApi.callList(accountId);
    }

    public static Folder getFolderIdByName(List<Folder> folders, String targetName) {
        for (Folder folder : folders) {
            if (folder.getName().equals(targetName)) {
                return folder;
            }

            if (folder.getFolders() != null && !folder.getFolders().isEmpty()) {
                Folder nestedFolder = DeleteRestoreEnvelopeService.getFolderIdByName(folder.getFolders(), targetName);
                if (nestedFolder != null) {
                    return nestedFolder;
                }
            }
        }
        return null;
    }
    //ds-snippet-end:eSign45Step5
}

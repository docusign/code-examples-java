package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.FoldersApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.Folder;
import com.docusign.esign.model.FoldersRequest;
import com.docusign.esign.model.FoldersResponse;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        var moveEnvelopes = foldersApi.moveEnvelopesWithHttpInfo(accountId, RECYCLE_BIN_FOLDER_ID, foldersRequest);
        Map<String, List<String>> headers = moveEnvelopes.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return moveEnvelopes.getData();
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

        var moveEnvelopesResponse = foldersApi.moveEnvelopesWithHttpInfo(accountId, folderId, foldersRequest);
        Map<String, List<String>> headers = moveEnvelopesResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return moveEnvelopesResponse.getData();
    }
    //ds-snippet-end:eSign45Step6

    //ds-snippet-start:eSign45Step5

    public static FoldersResponse getFolders(
            ApiClient apiClient,
            String accountId) throws Exception {
        FoldersApi foldersApi = new FoldersApi(apiClient);

        var callListResponse = foldersApi.callListWithHttpInfo(accountId, foldersApi.new CallListOptions());
        Map<String, List<String>> headers = callListResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return callListResponse.getData();
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

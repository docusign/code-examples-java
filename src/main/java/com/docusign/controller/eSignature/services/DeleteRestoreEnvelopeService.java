package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.FoldersApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.FoldersRequest;
import com.docusign.esign.model.FoldersResponse;

import java.util.Collections;

public final class DeleteRestoreEnvelopeService {
    public static FoldersResponse moveEnvelopeToFolder(
        ApiClient apiClient,
        String accountId,
        String envelopeId,
        String folderId,
        String fromFolderId
    ) throws Exception {
        FoldersApi foldersApi = new FoldersApi(apiClient);

        FoldersRequest foldersRequest = new FoldersRequest();
        foldersRequest.setFromFolderId(fromFolderId);
        foldersRequest.setEnvelopeIds(Collections.singletonList(envelopeId));

        return foldersApi.moveEnvelopes(accountId, folderId, foldersRequest);
    }

    public static FoldersResponse getFolders(
        ApiClient apiClient,
        String accountId
    ) throws Exception {
        FoldersApi foldersApi = new FoldersApi(apiClient);
        return foldersApi.callList(accountId);
    }
}

package com.docusign.controller.workspaces.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.AddWorkspaceDocumentRequest;
import com.docusign.iam.sdk.models.components.AddWorkspaceDocumentRequestFile;
import com.docusign.iam.sdk.models.operations.AddWorkspaceDocumentResponse;

public final class AddDocumentToWorkspaceService {
    private static IamClient createIamClient(String accessToken) {
        return IamClient.builder().accessToken(accessToken).build();
    }

    public static AddWorkspaceDocumentResponse addDocumentToWorkspace(
            String accessToken,
            String accountId,
            String workspaceId,
            String documentPath,
            String documentName) throws Exception {
        
        var resource = new ClassPathResource(documentPath);
        var documentBytes = StreamUtils.copyToByteArray(resource.getInputStream());

        var workspaceDocumentFile = new AddWorkspaceDocumentRequestFile(documentName, documentBytes);
        var addWorkspaceDocumentRequest = new AddWorkspaceDocumentRequest();
        addWorkspaceDocumentRequest.withFile(workspaceDocumentFile);
        
        return createIamClient(accessToken)
            .workspaces()
            .workspaceDocuments()
            .addWorkspaceDocument()
            .accountId(accountId)
            .workspaceId(workspaceId)
            .addWorkspaceDocumentRequest(addWorkspaceDocumentRequest)
            .call();
    }
}

package com.docusign.controller.workspaces.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.CreateWorkspaceBody;
import com.docusign.iam.sdk.models.operations.CreateWorkspaceResponse;

public final class CreateWorkspaceService {    
    private static IamClient createIamClient(String accessToken) {
        return IamClient.builder().accessToken(accessToken).build();
    }

    public static CreateWorkspaceResponse createWorkspace(
            String accessToken,
            String accountId,
            String name) throws Exception {
        var createWorkspaceBody = new CreateWorkspaceBody();
        createWorkspaceBody.withName(name);
        
        return createIamClient(accessToken)
            .workspaces()
            .workspaces()
            .createWorkspace()
            .accountId(accountId)
            .createWorkspaceBody(createWorkspaceBody)
            .call();
    }
}

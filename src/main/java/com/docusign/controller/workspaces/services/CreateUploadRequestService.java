package com.docusign.controller.workspaces.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.*;
import com.docusign.iam.sdk.models.operations.CreateWorkspaceUploadRequestResponse;
import java.time.OffsetDateTime;
import java.util.Arrays;

public final class CreateUploadRequestService {
    private static IamClient createIamClient(String accessToken) {
        return IamClient.builder().accessToken(accessToken).build();
    }

    public static CreateWorkspaceUploadRequestResponse createUploadRequest(
            String accessToken,
            String accountId,
            String workspaceId,
            String creatorId,
            String assigneeEmail) throws Exception {
        OffsetDateTime dueDate = OffsetDateTime.now().plusDays(7);

        var assignmentFoAssignee = new CreateWorkspaceUploadRequestAssignment(
                WorkspaceUploadRequestResponsibilityType.ASSIGNEE)
                .withFirstName("Test")
                .withLastName("User")
                .withEmail(assigneeEmail);
        var assignmentForCreator = new CreateWorkspaceUploadRequestAssignment(
                WorkspaceUploadRequestResponsibilityType.WATCHER)
                .withAssigneeUserId(creatorId);
        var assignments = Arrays.asList(
                assignmentFoAssignee,
                assignmentForCreator);

        var createWorkspaceUploadRequestBody = new CreateWorkspaceUploadRequestBody(dueDate,
                WorkspaceUploadRequestStatus.DRAFT)
                .withName("Upload Request example " + dueDate)
                .withDescription("This is an example upload request created via the workspaces API")
                .withAssignments(assignments);

        return createIamClient(accessToken)
                .workspaces()
                .workspaceUploadRequest()
                .createWorkspaceUploadRequest()
                .accountId(accountId)
                .workspaceId(workspaceId)
                .createWorkspaceUploadRequestBody(createWorkspaceUploadRequestBody)
                .call();
    }
}

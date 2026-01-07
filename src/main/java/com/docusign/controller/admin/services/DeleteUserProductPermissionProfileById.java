package com.docusign.controller.admin.services;

import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.RemoveUserProductsResponse;
import com.docusign.admin.model.UserProductProfileDeleteRequest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeleteUserProductPermissionProfileById {
    public static RemoveUserProductsResponse deleteUserProductPermissionProfile(
            ProductPermissionProfilesApi productPermissionProfilesApi,
            UUID productId,
            String emailAddress,
            UUID organizationId,
            UUID accountId) throws Exception {

        //ds-snippet-start:Admin9Step4
        var userProductProfileDeleteRequest = new UserProductProfileDeleteRequest();
        userProductProfileDeleteRequest.setProductIds(Collections.singletonList(productId));
        userProductProfileDeleteRequest.setUserEmail(emailAddress);
        //ds-snippet-end:Admin9Step4

        ApiResponse<RemoveUserProductsResponse> response = productPermissionProfilesApi
                .removeUserProductPermissionWithHttpInfo(
                        organizationId,
                        accountId,
                        userProductProfileDeleteRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
package com.docusign.controller.admin.services;

import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.ProductPermissionProfileRequest;
import com.docusign.admin.model.UserProductPermissionProfilesRequest;
import com.docusign.admin.model.UserProductPermissionProfilesResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UpdateUserProductPermissionProfileByEmail {
    public static UserProductPermissionProfilesResponse updateUserProductPermissionProfile(
            ProductPermissionProfilesApi productPermissionProfilesApi,
            String permissionProfileId,
            UUID productId,
            String emailAddress,
            UUID organizationId,
            UUID accountId) throws Exception {
        //ds-snippet-start:Admin8Step3
        var productPermissionProfileRequest = new ProductPermissionProfileRequest();
        productPermissionProfileRequest.setPermissionProfileId(permissionProfileId);
        productPermissionProfileRequest.setProductId(productId);

        var userProductPermissionProfilesRequest = new UserProductPermissionProfilesRequest();
        userProductPermissionProfilesRequest.email(emailAddress);
        userProductPermissionProfilesRequest.addProductPermissionProfilesItem(productPermissionProfileRequest);
        //ds-snippet-end:Admin8Step3

        //ds-snippet-start:Admin8Step4
        ApiResponse<UserProductPermissionProfilesResponse> response = productPermissionProfilesApi
                .addUserProductPermissionProfilesByEmailWithHttpInfo(
                        organizationId,
                        accountId,
                        userProductPermissionProfilesRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:Admin8Step4
    }
}

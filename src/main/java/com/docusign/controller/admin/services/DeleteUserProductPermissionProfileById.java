package com.docusign.controller.admin.services;

import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.model.RemoveUserProductsResponse;
import com.docusign.admin.model.UserProductProfileDeleteRequest;

import java.util.Collections;
import java.util.UUID;

public class DeleteUserProductPermissionProfileById {
    public static RemoveUserProductsResponse deleteUserProductPermissionProfile(
            ProductPermissionProfilesApi productPermissionProfilesApi,
            UUID productId,
            String emailAddress,
            UUID organizationId,
            UUID accountId
    ) throws Exception {

        //ds-snippet-start:Admin9Step4
        var userProductProfileDeleteRequest = new UserProductProfileDeleteRequest();
        userProductProfileDeleteRequest.setProductIds(Collections.singletonList(productId));
        userProductProfileDeleteRequest.setUserEmail(emailAddress);
        //ds-snippet-end:Admin9Step4

        return productPermissionProfilesApi.removeUserProductPermission(
                organizationId,
                accountId,
                userProductProfileDeleteRequest);
    }
}
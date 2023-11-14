package com.docusign.controller.admin.services;

import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.model.ProductPermissionProfileRequest;
import com.docusign.admin.model.UserProductPermissionProfilesRequest;
import com.docusign.admin.model.UserProductPermissionProfilesResponse;

import java.util.UUID;

public class UpdateUserProductPermissionProfileByEmail {
    public static UserProductPermissionProfilesResponse updateUserProductPermissionProfile(
            ProductPermissionProfilesApi productPermissionProfilesApi,
            String permissionProfileId,
            UUID productId,
            String emailAddress,
            UUID organizationId,
            UUID accountId
    ) throws Exception {
        //ds-snippet-start:Admin8Step3
        var productPermissionProfileRequest = new ProductPermissionProfileRequest();
        productPermissionProfileRequest.setPermissionProfileId(permissionProfileId);
        productPermissionProfileRequest.setProductId(productId);

        var userProductPermissionProfilesRequest = new UserProductPermissionProfilesRequest();
        userProductPermissionProfilesRequest.email(emailAddress);
        userProductPermissionProfilesRequest.addProductPermissionProfilesItem(productPermissionProfileRequest);
        //ds-snippet-end:Admin8Step3

        //ds-snippet-start:Admin8Step4
        return productPermissionProfilesApi.addUserProductPermissionProfilesByEmail(
                organizationId,
                accountId,
                userProductPermissionProfilesRequest);
        //ds-snippet-end:Admin8Step4
    }
}

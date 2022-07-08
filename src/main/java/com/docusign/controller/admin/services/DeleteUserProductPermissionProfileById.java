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
	   var userProductProfileDeleteRequest = new UserProductProfileDeleteRequest();
	   userProductProfileDeleteRequest.setProductIds(Collections.singletonList(productId));
	   userProductProfileDeleteRequest.setUserEmail(emailAddress);

	   return productPermissionProfilesApi.removeUserProductPermission(
			 organizationId,
			 accountId,
			 userProductProfileDeleteRequest);
    }
}
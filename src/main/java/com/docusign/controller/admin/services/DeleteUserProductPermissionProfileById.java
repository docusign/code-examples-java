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

	   // Step 4 start
	   var userProductProfileDeleteRequest = new UserProductProfileDeleteRequest();
	   userProductProfileDeleteRequest.setProductIds(Collections.singletonList(productId));
	   userProductProfileDeleteRequest.setUserEmail(emailAddress);
	   // Step 4 end
	   return productPermissionProfilesApi.removeUserProductPermission(
			 organizationId,
			 accountId,
			 userProductProfileDeleteRequest);
    }
}
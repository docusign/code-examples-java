package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.AddUserResponse;
import com.docusign.admin.model.DSGroupRequest;
import com.docusign.admin.model.NewMultiProductUserAddRequest;
import com.docusign.admin.model.ProductPermissionProfileRequest;

import java.util.UUID;

public class CreateActiveCLMESignUserService {
    public static AddUserResponse createNewActiveUser(
            String clmProfileId,
            UUID clmProductId,
            String eSignProfileId,
            UUID eSignProductId,
            UUID dsGroupId,
            String userName,
            String firstName,
            String lastName,
            String email,
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId
    ) throws Exception {
        //ds-snippet-start:Admin2Step5
        ProductPermissionProfileRequest clm = new ProductPermissionProfileRequest();
        ProductPermissionProfileRequest eSign = new ProductPermissionProfileRequest();
        clm.setPermissionProfileId(clmProfileId);
        clm.setProductId(clmProductId);

        eSign.setPermissionProfileId(eSignProfileId);
        eSign.setProductId(eSignProductId);

        DSGroupRequest dsGroup = new DSGroupRequest();
        dsGroup.setDsGroupId(dsGroupId);
        // Fill the request with data from the form

        NewMultiProductUserAddRequest multiProductUserAddRequest = new NewMultiProductUserAddRequest()
                .defaultAccountId(accountId)
                .addProductPermissionProfilesItem(clm)
                .addProductPermissionProfilesItem(eSign)
                .addDsGroupsItem(dsGroup)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .autoActivateMemberships(true)
                .email(email);
        //ds-snippet-end:Admin2Step5

        //ds-snippet-start:Admin2Step6
        return usersApi.addOrUpdateUser(organizationId, accountId, multiProductUserAddRequest);
        //ds-snippet-end:Admin2Step6
    }
}

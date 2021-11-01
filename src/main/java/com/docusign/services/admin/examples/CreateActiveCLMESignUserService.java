package com.docusign.services.admin.examples;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.AddUserResponse;
import com.docusign.admin.model.DSGroupRequest;
import com.docusign.admin.model.NewMultiProductUserAddRequest;
import com.docusign.admin.model.ProductPermissionProfileRequest;
import com.docusign.common.WorkArguments;

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
            UUID accountId) throws Exception
    {
        // Step 5 start
        ProductPermissionProfileRequest clm = new ProductPermissionProfileRequest();
        ProductPermissionProfileRequest eSign = new ProductPermissionProfileRequest();
        clm.setPermissionProfileId(clmProfileId);
        clm.setProductId(clmProductId);

        eSign.setPermissionProfileId(eSignProfileId);
        eSign.setProductId(eSignProductId);

        DSGroupRequest dsGroup = new DSGroupRequest();
        dsGroup.setDsGroupId(dsGroupId);
        // Fill the request with data from the form

        NewMultiProductUserAddRequest accountAddRequest = new NewMultiProductUserAddRequest()
                .defaultAccountId(accountId)
                .addProductPermissionProfilesItem(clm)
                .addProductPermissionProfilesItem(eSign)
                .addDsGroupsItem(dsGroup)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .autoActivateMemberships(true)
                .email(email);
        // Step 5 end

        // Step 6 start
        return usersApi.addOrUpdateUser(organizationId, accountId, accountAddRequest);
        // Step 6 end
    }
}

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
            WorkArguments args,
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId) throws Exception
    {
        // Step 5 start
        ProductPermissionProfileRequest clm = new ProductPermissionProfileRequest();
        ProductPermissionProfileRequest eSign = new ProductPermissionProfileRequest();
        clm.setPermissionProfileId(args.getClmProfileId());
        clm.setProductId(args.getClmProductId());

        eSign.setPermissionProfileId(args.getESignProfileId());
        eSign.setProductId(args.getESignProductId());

        DSGroupRequest dsGroup = new DSGroupRequest();
        dsGroup.setDsGroupId(args.getDsGroupId());
        // Fill the request with data from the form

        NewMultiProductUserAddRequest accountAddRequest = new NewMultiProductUserAddRequest()
                .defaultAccountId(accountId)
                .addProductPermissionProfilesItem(clm)
                .addProductPermissionProfilesItem(eSign)
                .addDsGroupsItem(dsGroup)
                .userName(args.getUserName())
                .firstName(args.getFirstName())
                .lastName(args.getLastName())
                .autoActivateMemberships(true)
                .email(args.getEmail());
        // Step 5 end

        // Step 6 start
        return usersApi.addOrUpdateUser(organizationId, accountId, accountAddRequest);
        // Step 6 end
    }
}

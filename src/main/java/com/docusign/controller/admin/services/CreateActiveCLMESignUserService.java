package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.AddUserResponse;
import com.docusign.admin.model.DSGroupRequest;
import com.docusign.admin.model.NewMultiProductUserAddRequest;
import com.docusign.admin.model.ProductPermissionProfileRequest;

import java.time.Instant;
import java.util.List;
import java.util.Map;
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
            UUID accountId) throws Exception {
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
        ApiResponse<AddUserResponse> response = usersApi.addOrUpdateUserWithHttpInfo(organizationId, accountId,
                multiProductUserAddRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:Admin2Step6
    }
}

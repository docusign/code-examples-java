package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.PermissionProfileResponse21;
import com.docusign.admin.model.ProductPermissionProfileResponse;
import com.docusign.admin.model.RemoveUserProductsResponse;
import com.docusign.admin.model.UserProductPermissionProfilesResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.DeleteUserProductPermissionProfileById;
import com.docusign.controller.admin.services.RetrieveDocuSignProfileByEmailAddress;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This example demonstrates how to delete user product permission profiles using an email address.
 */
@Controller
@RequestMapping("/a009")
public class A009DeleteUserProductPermissionProfile extends AbstractAdminController {
    public static UUID clmProductId = UUID.fromString("37f013eb-7012-4588-8028-357b39fdbd00");

    public static UUID eSignatureProductId = UUID.fromString("f6406c68-225c-4e9b-9894-64152a26fa83");

    private final Hashtable<UUID, String> products = new Hashtable<>();

    private final String CLM_PROFILES_NOT_FOUND = "No CLM permission profiles are connected to this user";

    private final String ESIGN_PROFILES_NOT_FOUND = "No eSignature permission profiles are connected to this user";

    public A009DeleteUserProductPermissionProfile(DSConfiguration config, Session session, User user) {
        super(config, "a009", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID existingAccountId = this.getExistingAccountId(user.getAccessToken(), session.getBasePath(), organizationId);
        String emailAddress = this.session.getEmailAddress();
        List<String> clmProfiles = null;
        List<String> eSignProfiles = null;

        if (emailAddress != null) {
            try {

                UsersApi usersApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());
                RetrieveDocuSignProfileByEmailAddress.getDocuSignProfileByEmailAddress(usersApi, organizationId, emailAddress);

                //ds-snippet-start:Admin9Step3
                ProductPermissionProfilesApi productPermissionProfilesApi = this.createProductPermissionProfilesApi(
                        user.getAccessToken(),
                        session.getBasePath()
                );
                ProductPermissionProfilesApi.GetUserProductPermissionProfilesByEmailOptions userProductPermissionProfilesByEmailOptions =
                        productPermissionProfilesApi.new GetUserProductPermissionProfilesByEmailOptions();
                userProductPermissionProfilesByEmailOptions.setEmail(emailAddress);
                UserProductPermissionProfilesResponse userProductPermissionProfilesByEmail = productPermissionProfilesApi
                        .getUserProductPermissionProfilesByEmail(organizationId, existingAccountId, userProductPermissionProfilesByEmailOptions);
                //ds-snippet-end:Admin9Step3
                for (ProductPermissionProfileResponse profileResponse : userProductPermissionProfilesByEmail.getProductPermissionProfiles()) {
                    if (profileResponse.getProductName().equals("CLM")) {
                        clmProfiles = profileResponse
                                .getPermissionProfiles()
                                .stream()
                                .map(PermissionProfileResponse21::getPermissionProfileName)
                                .collect(Collectors.toList());
                    } else {
                        eSignProfiles = profileResponse
                                .getPermissionProfiles()
                                .stream()
                                .map(PermissionProfileResponse21::getPermissionProfileName)
                                .collect(Collectors.toList());
                    }
                }

                String clmProfilesFormatted = clmProfiles != null
                        ? String.join(",", clmProfiles)
                        : CLM_PROFILES_NOT_FOUND;

                String eSignProfilesFormatted = eSignProfiles != null
                        ? String.join(",", eSignProfiles)
                        : ESIGN_PROFILES_NOT_FOUND;

                products.put(eSignatureProductId, String.format("eSignature - %s", eSignProfilesFormatted));
                products.put(clmProductId, String.format("CLM - %s", clmProfilesFormatted));

                model.addAttribute("listProducts", products);

                model.addAttribute("emailAddress", emailAddress);
            } catch (ApiException e) {
                model.addAttribute("emailAddress", null);
            }
        } else {
            model.addAttribute("emailAddress", null);
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID existingAccountId = this.getExistingAccountId(this.user.getAccessToken(), this.session.getBasePath(), organizationId);

        ProductPermissionProfilesApi productPermissionProfilesApi = createProductPermissionProfilesApi(
                this.user.getAccessToken(),
                this.session.getBasePath());

        //ds-snippet-start:Admin9Step5
        RemoveUserProductsResponse removeUserProductsRepsonse = DeleteUserProductPermissionProfileById.deleteUserProductPermissionProfile(
                productPermissionProfilesApi,
                args.getProductId(),
                this.session.getEmailAddress(),
                organizationId,
                existingAccountId);
        //ds-snippet-end:Admin9Step5
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(removeUserProductsRepsonse)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }

}

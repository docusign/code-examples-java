package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.*;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.RetrieveDocuSignProfileByEmailAddress;
import com.docusign.controller.admin.services.UpdateUserProductPermissionProfileByEmail;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

/**
 * This example demonstrates how to update user product permission profiles using an email address.
 */
@Controller
@RequestMapping("/a008")
public class A008UpdateUserProductPermissionProfile extends AbstractAdminController {

    private final User user;
    private final Hashtable<UUID, String> products = new Hashtable<>();
    private ProductPermissionProfilesResponse productPermissionProfiles;

    @Autowired
    public A008UpdateUserProductPermissionProfile(DSConfiguration config, Session session, User user) {
	   super(config, "a008");
	   this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
	   super.onInitModel(args, model);
	   UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
	   UUID existingAccountId = this.getExistingAccountId(user.getAccessToken(), session.getBasePath(), organizationId);
	   String emailAddress = this.session.getEmailAddress();
	   ProductPermissionProfileResponse permissionProfiles = null;
	   UUID clmProductId = null;
	   UUID eSignProductId = null;

	   if (emailAddress != null) {
	       try {
			 UsersApi usersApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());
			 RetrieveDocuSignProfileByEmailAddress.getDocuSignProfileByEmailAddress(usersApi, organizationId, emailAddress);

			 ProductPermissionProfilesApi productPermissionProfilesApi = this.createProductPermissionProfilesApi(
				    user.getAccessToken(),
				    session.getBasePath()
			 );
			 productPermissionProfiles = productPermissionProfilesApi.getProductPermissionProfiles(organizationId, existingAccountId);

			 for (ProductPermissionProfileResponse profileResponse : productPermissionProfiles.getProductPermissionProfiles()) {
				if (profileResponse.getProductName().equals("CLM")) {
				    clmProductId = profileResponse.getProductId();
				} else {
				    permissionProfiles = profileResponse;
				    eSignProductId = profileResponse.getProductId();
				}
			 }

			 products.put(eSignProductId, "eSignature");
			 products.put(clmProductId, "CLM");

			 model.addAttribute("listPermissionProfiles", permissionProfiles);
			 model.addAttribute("listProducts", products);
			 model.addAttribute("emailAddress", emailAddress);
		  } catch(Exception e) {
			 model.addAttribute("emailAddress", null);
		  }
	   } else {
		  model.addAttribute("emailAddress", null);
	   }
    }

    @RequestMapping(value = "/getPermissionProfiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<PermissionProfileResponse21> getPermissionProfiles(@RequestParam(value = "productId") UUID productId) {
	   List<PermissionProfileResponse21> permissionProfiles = null;
	   for (ProductPermissionProfileResponse profileResponse : productPermissionProfiles.getProductPermissionProfiles()) {
		  if (profileResponse.getProductId().equals(productId)) {
			 permissionProfiles = profileResponse.getPermissionProfiles();
		  }
	   }

	   return permissionProfiles;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
	   UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
	   UUID existingAccountId = this.getExistingAccountId(
			 this.user.getAccessToken(),
			 this.session.getBasePath(),
			 organizationId
	   );

	   ProductPermissionProfilesApi productPermissionProfilesApi = createProductPermissionProfilesApi(
			 this.user.getAccessToken(),
			 this.session.getBasePath());

	   UserProductPermissionProfilesResponse productPermissionProfile = UpdateUserProductPermissionProfileByEmail
			 .updateUserProductPermissionProfile(
				    productPermissionProfilesApi,
				    args.getPermissionProfileId(),
				    args.getProductId(),
				    this.session.getEmailAddress(),
				    organizationId,
				    existingAccountId
			 );

	   DoneExample.createDefault(this.codeExampleText.ExampleName)
			 .withMessage(this.codeExampleText.ResultsPageText)
			 .withJsonObject(productPermissionProfile)
			 .addToModel(model);

	   return DONE_EXAMPLE_PAGE;
    }

}

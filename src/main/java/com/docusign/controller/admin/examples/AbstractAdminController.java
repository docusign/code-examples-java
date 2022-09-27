package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.AccountsApi;
import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.api.DsGroupsApi;
import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.api.UsersApi.GetUserProfilesOptions;
import com.docusign.admin.client.ApiClient;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.client.auth.AccessTokenListener;
import com.docusign.admin.client.auth.OAuth.Account;
import com.docusign.admin.client.auth.OAuth.Organization;
import com.docusign.admin.model.OrganizationAccountsRequest;
import com.docusign.admin.model.OrganizationsResponse;
import com.docusign.admin.model.ProductPermissionProfileResponse;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.esign.api.GroupsApi;
//import com.docusign.esign.client.ApiClient;
import com.docusign.esign.api.OrganizationsApi;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Admin controllers.
 */
@Controller
public abstract class AbstractAdminController extends AbstractController {

  private final DSConfiguration configuration;
  private static final String EXAMPLE_PAGES_PATH = "pages/admin/examples/";

  public AbstractAdminController(DSConfiguration config, String exampleName) {
    super(config, exampleName);
    this.configuration = config;
  }

  protected String getExamplePagesPath() {
    return AbstractAdminController.EXAMPLE_PAGES_PATH;
  }

  /**
   * Creates new instance of the Admin API client.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link ApiClient}
   */
  protected static ApiClient createApiClient(
    String accessToken,
    String basePath
  ) {
    // Step 2 start
    ApiClient apiClient = new ApiClient(basePath);
    apiClient.addDefaultHeader(
      HttpHeaders.AUTHORIZATION,
      BEARER_AUTHENTICATION + accessToken
    );
    // Step 2 end

    return apiClient;
  }

  /**
   * Creates a new instance of the UsersApi. This method
   * creates an instance of the UsersApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link UsersApi}
   */
  protected static UsersApi createUsersApi(
    String accessToken,
    String basePath
  ) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new UsersApi(apiClient);
  }

  /**
   * Creates a new instance of the BulkExportsApi. This method
   * creates an instance of the BulkExportsApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link BulkExportsApi}
   */
  protected static BulkExportsApi createBulkExportsApi(
    String accessToken,
    String basePath
  ) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new BulkExportsApi(apiClient);
  }

  /**
   * Creates a new instance of the BulkImportsApi. This method
   * creates an instance of the BulkImportsApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link BulkImportsApi}
   */
  protected static BulkImportsApi createBulkImportsApi(
    String accessToken,
    String basePath
  ) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new BulkImportsApi(apiClient);
  }

  /**
   * Creates a request to get current user.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @param organizationId the ID of current organization
   * @return the account Id
   */
  protected UUID getExistingAccountId(
    String accessToken,
    String basePath,
    UUID organizationId
  )
    throws Exception {
    UsersApi usersApi = createUsersApi(accessToken, basePath);

    // set the signer email to get an information about that user
    // new GetUsersOptions() will throw "an enclosing instance that contains com.docusign.admin.api.UsersApi.GetUserProfilesOptions is required"
    // at compile time.  I used this workaround from stackOverflow:
    // https://stackoverflow.com/a/4297913/2226328

    GetUserProfilesOptions userProfilesOptions =
      usersApi.new GetUserProfilesOptions();
    userProfilesOptions.setEmail(configuration.getSignerEmail());

    UsersDrilldownResponse user = usersApi.getUserProfiles(
      organizationId,
      userProfilesOptions
    );

    if (user.getUsers().isEmpty()) {
      throw new Exception("Could not get an account id from the request.");
    }

    return user.getUsers().get(0).getDefaultAccountId();
  }

  protected UUID getOrganizationId(String accessToken, String basePath)
    throws ApiException {
    UUID orgId = null;
    if (session.getOrgId() == null) {

        ApiClient apiClient = createApiClient(accessToken, basePath);
        AccountsApi accounts = new AccountsApi(apiClient);
        OrganizationsResponse orgs = accounts.getOrganizations();
        if (orgs.getOrganizations().isEmpty()) {
          throw new ApiException(
            "No organizations found on this account, please <a href='https://admindemo.docusign.com/create-organization'>create an organization first.</a>"
          );
        } else {
            orgId = orgs.getOrganizations().get(0).getId();
            session.setOrgId(orgId);
        }
      
    } else {
      orgId = session.getOrgId();
    }

    return orgId;
  }

  /**
   * Creates a new instance of the ProductPermissionProfilesApi. This method
   * creates an instance of the ProductPermissionProfilesApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link ProductPermissionProfilesApi}
   */
  protected ProductPermissionProfilesApi createProductPermissionProfilesApi(
    String accessToken,
    String basePath
  ) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new ProductPermissionProfilesApi(apiClient);
  }

  /**
   * Creates a new instance of the AdminGroupsApi. This method
   * creates an instance of the AdminGroupsApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link AdminGroupsApi}
   */
  protected DsGroupsApi createDSGroupsApi(String accessToken, String basePath) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new DsGroupsApi(apiClient);
  }
}

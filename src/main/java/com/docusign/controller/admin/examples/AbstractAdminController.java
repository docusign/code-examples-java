package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.*;
import com.docusign.admin.api.UsersApi.GetUserProfilesOptions;
import com.docusign.admin.client.ApiClient;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.OrganizationsResponse;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.core.controller.AbstractController;
import java.util.UUID;

import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Admin controllers.
 */
@Controller
public abstract class AbstractAdminController extends AbstractController {

  private final DSConfiguration configuration;
  protected final User user;
  protected final Session session;
  private static final String EXAMPLE_PAGES_PATH = "pages/admin/examples/";

  public AbstractAdminController(DSConfiguration config, String exampleName, User user, Session session) {
    super(config, exampleName);
    this.configuration = config;
    this.user = user;
    this.session = session;
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
    //ds-snippet-start:AdminJavaStep2
    ApiClient apiClient = new ApiClient(basePath);
    apiClient.addDefaultHeader(
      HttpHeaders.AUTHORIZATION,
      BEARER_AUTHENTICATION + accessToken );
    //ds-snippet-end:AdminJavaStep2

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
    apiClient.addDefaultHeader("Content-Type", "application/json");
    return new UsersApi(apiClient);
  }

  /**
   * Creates a new instance of the AccountsApi. This method
   * creates an instance of the AccountsApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link AccountsApi}
   */
  protected static AccountsApi createAccountsApi(
          String accessToken,
          String basePath
  ) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    apiClient.addDefaultHeader("Content-Type", "application/json");
    return new AccountsApi(apiClient);
  }

  /**
   * Creates a new instance of the OrganizationsApi. This method
   * creates an instance of the OrganizationsApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link OrganizationsApi}
   */
  protected static OrganizationsApi createOrganizationsApi(
          String accessToken,
          String basePath
  ) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    apiClient.addDefaultHeader("Content-Type", "application/json");
    return new OrganizationsApi(apiClient);
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
      System.out.println("createUsers API called");
    UsersApi usersApi = createUsersApi(accessToken, basePath);
    System.out.println("createUsers called succeeds");
    // set the signer email to get an information about that user
    // new GetUsersOptions() will throw "an enclosing instance that contains com.docusign.admin.api.UsersApi.GetUserProfilesOptions is required"
    // at compile time.  I used this workaround from stackOverflow:
    // https://stackoverflow.com/a/4297913/2226328
    System.out.println("what email are we feedin ya? hmm??? " + configuration.getSignerEmail());
    GetUserProfilesOptions userProfilesOptions =
      usersApi.new GetUserProfilesOptions();
    userProfilesOptions.setEmail(configuration.getSignerEmail());
    System.out.println("getUserProfiles called");
    UsersDrilldownResponse user = usersApi.getUserProfiles(
      organizationId,
      userProfilesOptions
    );
    System.out.println("getUserProfiles succeeded");
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
        System.out.println("before Org id");
        OrganizationsResponse orgs = accounts.getOrganizations();
        System.out.println("after orgs call");
        if (orgs.getOrganizations().isEmpty()) {
          throw new ApiException(
            "No organizations found on this account, please <a href='https://admindemo.docusign.com/create-organization'>create an organization first.</a>"
          );
        } else {
          System.out.println("we're looking for a specific org id");
            orgId = orgs.getOrganizations().get(0).getId();
            System.out.println("first found org id is: "+ orgId);
            session.setOrgId(orgId);
        }

    } else {
      System.out.println("nothing found? try a 2nd way");
      orgId = session.getOrgId();

      System.out.println("we have an org id: "+ orgId);
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
   * @return an instance of the {@link DsGroupsApi}
   */
  protected DsGroupsApi createDSGroupsApi(String accessToken, String basePath) {
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new DsGroupsApi(apiClient);
  }

  /**
   * Creates a new instance of the ProvisionAssetGroupApi. This method
   * creates an instance of the ProvisionAssetGroupApi class silently.
   * @param accessToken user's access token
   * @param basePath basePath to the server
   * @return an instance of the {@link ProvisionAssetGroupApi}
   */
  protected ProvisionAssetGroupApi createProvisionAssetGroupApi(String accessToken, String basePath) {
    //ds-snippet-start:Admin12Step2
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new ProvisionAssetGroupApi(apiClient);
    //ds-snippet-end:Admin12Step2
  }
}

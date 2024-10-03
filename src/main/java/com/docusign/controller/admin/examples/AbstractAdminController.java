package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.*;
import com.docusign.admin.api.UsersApi.GetUserProfilesOptions;
import com.docusign.admin.client.ApiClient;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.OrganizationsResponse;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Abstract base class for all Admin controllers.
 */
@Controller
public abstract class AbstractAdminController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdminController.class);

    private static final String EXAMPLE_PAGES_PATH = "pages/admin/examples/";

    protected final User user;

    protected final Session session;

    private final DSConfiguration configuration;


    public AbstractAdminController(DSConfiguration config, String exampleName, User user, Session session) {
        super(config, exampleName);
        this.configuration = config;
        this.user = user;
        this.session = session;
    }

    /**
     * Creates new instance of the Admin API client.
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
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
                BEARER_AUTHENTICATION + accessToken);
        //ds-snippet-end:AdminJavaStep2

        return apiClient;
    }

    /**
     * Creates a new instance of the UsersApi. This method
     * creates an instance of the UsersApi class silently.
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
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
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
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
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
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
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
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
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
     * @return an instance of the {@link BulkImportsApi}
     */
    protected static BulkImportsApi createBulkImportsApi(
            String accessToken,
            String basePath
    ) {
        ApiClient apiClient = createApiClient(accessToken, basePath);
        return new BulkImportsApi(apiClient);
    }

    protected String getExamplePagesPath() {
        return AbstractAdminController.EXAMPLE_PAGES_PATH;
    }

    /**
     * Creates a request to get current user.
     *
     * @param accessToken    user's access token
     * @param basePath       basePath to the server
     * @param organizationId the ID of current organization
     * @return the account Id
     */
    protected UUID getExistingAccountId(
            String accessToken,
            String basePath,
            UUID organizationId
    ) throws Exception {
        LOGGER.info("createUsers API called");
        UsersApi usersApi = createUsersApi(accessToken, basePath);
        LOGGER.info("createUsers called succeeds");
        // set the signer email to get an information about that user
        // new GetUsersOptions() will throw "an enclosing instance that contains com.docusign.admin.api.UsersApi.GetUserProfilesOptions is required"
        // at compile time.  I used this workaround from stackOverflow:
        // https://stackoverflow.com/a/4297913/2226328
        LOGGER.info("what email are we feedin ya? hmm??? " + configuration.getSignerEmail());
        GetUserProfilesOptions userProfilesOptions =
                usersApi.new GetUserProfilesOptions();
        userProfilesOptions.setEmail(configuration.getSignerEmail());
        LOGGER.info("getUserProfiles called");
        UsersDrilldownResponse user = usersApi.getUserProfiles(
                organizationId,
                userProfilesOptions
        );
        LOGGER.info("getUserProfiles succeeded");
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
            LOGGER.info("before Org id");
            OrganizationsResponse orgs = accounts.getOrganizations();
            LOGGER.info("after orgs call");
            if (orgs.getOrganizations().isEmpty()) {
                throw new ApiException(
                    "No organizations found on this account, please <a href='https://admindemo.docusign.com/create-organization'>create an organization first.</a>"
                );
            } else {
                LOGGER.info("we're looking for a specific org id");
                orgId = orgs.getOrganizations().get(0).getId();
                LOGGER.info("first found org id is: " + orgId);
                session.setOrgId(orgId);
            }

        } else {
            LOGGER.info("nothing found? try a 2nd way");
            orgId = session.getOrgId();

            LOGGER.info("we have an org id: " + orgId);
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
    //ds-snippet-start:AdminJavaAccountStep2
    ApiClient apiClient = createApiClient(accessToken, basePath);
    return new ProvisionAssetGroupApi(apiClient);
    //ds-snippet-end:AdminJavaAccountStep2
  }
}

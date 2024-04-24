package com.docusign.core.controller;

import com.docusign.DSConfiguration;
import com.docusign.core.model.*;
import com.docusign.core.utils.AccountsConverter;
import com.docusign.esign.client.auth.OAuth;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides common model attributes for all pages. If you want to
 * refresh tokens use the next code to access to expiration time:
 * <pre>
 * {@literal @}Autowired
 * private TokenStore tokenStore;
 * ....
 * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 * OAuth2Authentication oauth = (OAuth2Authentication) authentication;
 * OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oauth.getDetails();
 * String accessToken = details.getTokenValue();
 * OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
 * </pre>
 * Object <code>token</code> contains all necessary information.
 */
@ControllerAdvice
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GlobalControllerAdvice {

    private static final String ERROR_ACCOUNT_NOT_FOUND = "Could not find account information for the user";

    private static final String PATH_TO_HOMEPAGE = "/pages/esignature/index";

    private final DSConfiguration config;

    private final Session session;

    private final User user;

    private Optional<OAuth.Account> account;

    private final AuthType authTypeSelected = AuthType.AGC;

    private final ApiType apiTypeSelected = ApiType.ESIGNATURE;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public GlobalControllerAdvice(DSConfiguration config, Session session, User user, Optional<OAuth.Account> account) {
        this.config = config;
        this.session = session;
        this.user = user;
        this.account = account;
    }

    private static List<OAuth.Account> getOAuthAccounts(OAuth2User user) {
        List<Map<String, Object>> oauthAccounts = user.getAttribute("accounts");
        if (oauthAccounts == null) {
            return new ArrayList<>();
        }

        return oauthAccounts.stream()
                .map(AccountsConverter::convert)
                .collect(Collectors.toList());
    }

    private static OAuth.Account getDefaultAccount(List<OAuth.Account> accounts) {
        for (OAuth.Account oauthAccount : accounts) {
            if ("true".equals(oauthAccount.getIsDefault())) {
                return oauthAccount;
            }
        }
        return null;
    }

    private static OAuth.Account getAccountById(List<OAuth.Account> accounts, String accountId) {
        for (OAuth.Account oauthAccount : accounts) {
            if (StringUtils.equals(oauthAccount.getAccountId(), accountId)) {
                return oauthAccount;
            }
        }
        return null;
    }

    @ModelAttribute("authTypes")
    public List<AuthTypeItem> authTypes() {
        return AuthTypeItem.list();
    }

    @ModelAttribute("authTypeSelected")
    public AuthTypeItem authTypeSelected() {
        return AuthTypeItem.convert(authTypeSelected);
    }

    @ModelAttribute("apiTypes")
    public List<ApiTypeItem> apiTypes() {
        return ApiTypeItem.list();
    }

    @ModelAttribute("apiTypeSelected")
    public ApiTypeItem apiTypeSelected() {
        return ApiTypeItem.convert(apiTypeSelected);
    }

    @ModelAttribute("documentOptions")
    public List<JSONObject> populateDocumentOptions() {
        return new ArrayList<>();
    }

    @ModelAttribute("showDoc")
    public boolean populateShowDoc() {
        return StringUtils.isNotBlank(config.getDocumentationPath());
    }

    @ModelAttribute("locals")
    public Locals populateLocals() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        session.setApiIndexPath(PATH_TO_HOMEPAGE);

        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            return new Locals(config, session, null, "");
        }

        OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauth.getPrincipal();
        OAuth2AuthorizedClient oauthClient = authorizedClientService.loadAuthorizedClient(
                oauth.getAuthorizedClientRegistrationId(),
                oauthUser.getName()
        );

        if (oauth.isAuthenticated()) {
            user.setName(oauthUser.getAttribute("name"));

            if (oauthClient != null) {
                user.setAccessToken(oauthClient.getAccessToken().getTokenValue());
            } else {
                user.setAccessToken(((OAuth.OAuthToken) oauthUser.getAttribute("access_token")).getAccessToken());
            }

            if (account.isEmpty()) {
                account = Optional.ofNullable(getDefaultAccountInfo(getOAuthAccounts(oauthUser)));
            }

            OAuth.Account oauthAccount = account.orElseThrow(() -> new NoSuchElementException(ERROR_ACCOUNT_NOT_FOUND));
            session.setOauthAccount(oauthAccount);
            session.setAccountId(oauthAccount.getAccountId());
            session.setAccountName(oauthAccount.getAccountName());

            //TODO set this more efficiently with more APIs as they're added in
            String basePath = this.config.getBaseUrl(config.getSelectedApiIndex(), oauthAccount) + config.getSelectedApiIndex().getBaseUrlSuffix();
            session.setBasePath(basePath);
        }

        return new Locals(config, session, user, "");
    }

    private OAuth.Account getDefaultAccountInfo(List<OAuth.Account> accounts) {
        String targetAccountId = config.getTargetAccountId();
        if (StringUtils.isNotBlank(targetAccountId)) {
            OAuth.Account account = getAccountById(accounts, targetAccountId);
            if (account != null) {
                return account;
            }
        }
        return getDefaultAccount(accounts);
    }
}

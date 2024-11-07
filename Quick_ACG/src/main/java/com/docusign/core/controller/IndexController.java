package com.docusign.core.controller;

import com.docusign.DSConfiguration;
import com.docusign.core.model.Locals;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.docusign.core.security.acg.ACGAuthenticationMethod;

import com.docusign.core.utils.AccountsConverter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.docusign.esign.client.auth.OAuth;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

@Controller
@ControllerAdvice
@Scope(WebApplicationContext.SCOPE_SESSION)
public class IndexController {
    private static final List<String> ESIGNATURE_SCOPES = Arrays.asList("signature");
    private static final String ATTR_STATE = "state";
    private static final String ATTR_EVENT = "event";

    private static final String ERROR_ACCOUNT_NOT_FOUND = "Could not find account information for the user";
    private final DSConfiguration config;
    private final Session session;
    private final User user;
    private Optional<OAuth.Account> account;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public IndexController(DSConfiguration config, Session session, User user, Optional<OAuth.Account> account) {
        this.config = config;
        this.session = session;
        this.user = user;
        this.account = account;
    }

    @GetMapping(path = "/")
    public String index(ModelMap model, HttpServletResponse response) throws IOException {
        String site = "/eg001";
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
        return null;
    }

    @GetMapping(path = "/ds/mustAuthenticate")
    public ModelAndView mustAuthenticateController(ModelMap model) throws IOException, Exception {
        return new ModelAndView(getRedirectView());
    }

    @GetMapping(path = "/ds-return")
    public String returnController(@RequestParam(value = ATTR_STATE, required = false) String state,
            @RequestParam(value = ATTR_EVENT, required = false) String event,
            @RequestParam(required = false) String envelopeId, ModelMap model, HttpServletResponse response)
            throws IOException {
        String site = "/eg001";
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
        return null;
    }

    @GetMapping("/pkce")
    public RedirectView pkce(String code, String state, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String redirectURL = "/";
        RedirectView redirect;
        try {
            redirect = new ACGAuthenticationMethod().exchangeCodeForToken(code, config, session, redirectURL,
                    ESIGNATURE_SCOPES);
        } catch (Exception e) {
            redirect = new RedirectView(getLoginPath());
            this.session.setIsPKCEWorking(false);
        }

        return redirect;
    }

    private RedirectView getRedirectView() throws Exception {
        RedirectView redirect;
        if (this.session.getIsPKCEWorking()) {
            redirect = new ACGAuthenticationMethod().initiateAuthorization(config, ESIGNATURE_SCOPES);
        } else {
            redirect = new RedirectView(getLoginPath());
        }

        redirect.setExposeModelAttributes(false);
        return redirect;
    }

    private String getLoginPath() {
        return config.getAcgRedirectURL();
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
    public Object populateLocals() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            return new RedirectView("/");
        }

        OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauth.getPrincipal();
        OAuth2AuthorizedClient oauthClient = authorizedClientService.loadAuthorizedClient(
                oauth.getAuthorizedClientRegistrationId(),
                oauthUser.getName());

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
            session.setAccountId(oauthAccount.getAccountId());
            session.setAccountName(oauthAccount.getAccountName());
            // TODO set this more efficiently with more APIs as they're added in
            String basePath = this.getBaseUrl(oauthAccount) + "/restapi";
            session.setBasePath(basePath);
        }

        return new Locals(config, session, user, "");
    }

    private String getBaseUrl(OAuth.Account oauthAccount) {
        return oauthAccount.getBaseUri();
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

    public static OAuth.Account convert(Map<String, Object> account) {
        OAuth.Account res = new OAuth.Account();
        res.setAccountId((String) account.get("account_id"));
        res.setIsDefault(String.valueOf(account.get("is_default")));
        res.setAccountName((String) account.get("account_name"));
        res.setBaseUri((String) account.get("base_uri"));
        return res;
    }
}

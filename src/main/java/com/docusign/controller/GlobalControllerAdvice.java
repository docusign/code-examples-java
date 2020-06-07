package com.docusign.controller;

import com.docusign.DSConfiguration;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.model.Locals;
import com.docusign.model.Session;
import com.docusign.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@ControllerAdvice
@Scope("session")
public class GlobalControllerAdvice {

    private static final String BASE_URI_SUFFIX = "/restapi";
    private static final String ERROR_ACCOUNT_NOT_FOUND = "Could not find account information for the user";

    @Value("${DS_TARGET_ACCOUNT_ID:}")
    private String TargetAccountId;

    @Autowired
    DSConfiguration config;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    Session session;

    @Autowired
    User user;

    @Autowired(required = false)
    private OAuth.Account Account;

    @ModelAttribute("accessToken")
    public String getAccessToken() {
        return (String) httpSession.getAttribute("accessToken");
    }

    @ModelAttribute("basePath")
    public String getBaseUri() {
        return (String) httpSession.getAttribute("basePath");
    }

    @ModelAttribute("documentOptions")
    public List<JSONObject> populateDocumentOptions() {
        return new ArrayList<>();
    }

    @ModelAttribute("envelopeId")
    public String populateEnvelopeId() {
        return (String) httpSession.getAttribute("envelopeId");
    }

    @ModelAttribute("showDoc")
    public boolean populateShowDoc() {
        return (config.documentation != null);
    }

    @ModelAttribute("locals")
    public Locals populateLocals(HttpServletRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();


        Locals locals = new Locals();
        locals.setDsConfig(config);
        locals.setSession(session);
        locals.setMessages("");
        locals.setUser(null);

        if (!(authentication instanceof OAuth2Authentication)) {
            return locals;
        }

        OAuth2Authentication oauth = (OAuth2Authentication) authentication;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oauth.getDetails();
        Authentication user1 = oauth.getUserAuthentication();

        if (user1 != null && user1.isAuthenticated()) {
            user.setName(user1.getName());
            locals.setUser(user);
            user.setAccessToken(details.getTokenValue());
            httpSession.setAttribute("accessToken", details.getTokenValue());
//                user.refreshToken = accessToken.getRefreshToken().getValue();
//                user.tokenExpirationTimestamp = ((30 * 60 * 1000) + System.currentTimeMillis());

            if (Account == null) {
                LinkedHashMap list = (LinkedHashMap) oauth.getUserAuthentication().getDetails();
                ArrayList accounts = (ArrayList) list.get("accounts");
                Account = getDefaultAccountInfo(this.convetToAccounts(accounts));
                httpSession.setAttribute("Account", Account);
            }

            session.setAccountId(Account.getAccountId());
            session.setAccountName(Account.getAccountName());
            session.setBasePath(Account.getBaseUri() + BASE_URI_SUFFIX);

            httpSession.setAttribute("accountId", Account.getAccountId());
            httpSession.setAttribute("accountName", Account.getAccountName());
            httpSession.setAttribute("basePath", Account.getBaseUri() + BASE_URI_SUFFIX);
        }


        return locals;
    }

    private List<OAuth.Account> convetToAccounts(ArrayList accounts) {
        List<OAuth.Account> accts = new ArrayList<>();
        for (Object account : accounts) {
            LinkedHashMap acct = (LinkedHashMap) account;
            OAuth.Account acct1 = new OAuth.Account();
            acct1.setAccountId((String) acct.get("account_id"));
            acct1.setIsDefault(String.valueOf(acct.get("is_default")));
            acct1.setAccountName((String) acct.get("account_name"));
            acct1.setBaseUri((String) acct.get("base_uri"));
            accts.add(acct1);
        }

        return accts;
    }

    private OAuth.Account getDefaultAccountInfo(List<OAuth.Account> accounts) throws Exception {

        OAuth.Account account;

        if (!TargetAccountId.isEmpty()) {
            account = getAccountById(accounts);
            if (account == null)
                throw new Exception(ERROR_ACCOUNT_NOT_FOUND);
        } else {
            account = getDefaultAccount(accounts);
        }

        return account;
    }

    private OAuth.Account getDefaultAccount(List<OAuth.Account> accounts) {
        for (OAuth.Account account : accounts) {
            if (account.getIsDefault() == "true")
                return account;
        }
        return null;
    }

    private OAuth.Account getAccountById(List<OAuth.Account> accounts) {

        for (OAuth.Account account : accounts) {
            if (account.getAccountId() == TargetAccountId)
                return account;
        }

        return null;
    }
}
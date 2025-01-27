package com.docusign.core.controller;

import com.docusign.DSConfiguration;
import com.docusign.common.ApiIndex;
import com.docusign.core.common.Utils;
import com.docusign.core.model.ApiType;
import com.docusign.core.model.AuthType;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.core.security.acg.ACGAuthenticationMethod;
import com.docusign.core.security.jwt.JWTAuthenticationMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {

    public static final String LOCATION_HEADER = "Location";

    private static final String ATTR_ENVELOPE_ID = "qpEnvelopeId";

    private static final String ATTR_STATE = "state";

    private static final String ATTR_EVENT = "event";

    private static final String ATTR_TITLE = "title";

    private static final String LAUNCHER_TEXTS = "launcherTexts";

    private static final String CODE_EXAMPLE_GROUPS = "codeExampleGroups";

    private static final String API_DATA = "APIData";

    private static final String STATUS_CFR = "statusCFR";

    @Autowired
    private Session session;

    @Autowired
    private User user;

    @Autowired
    private RequestCache requestCache;

    @Autowired
    private DSConfiguration config;

    @GetMapping(path = "/")
    public String index(ModelMap model, HttpServletResponse response) throws Exception {
        model.addAttribute(ATTR_TITLE, "Home");

        Boolean isCFR = false;

        if (this.config.isShareAccessExampleScenario()) {
            this.config.setShareAccessExampleScenario(false);
            this.config.setAdditionalRedirect(true);

            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader(LOCATION_HEADER, "/ds/mustAuthenticate");
            return null;
        }

        if (this.config.isAdditionalRedirect()) {
            this.config.setAdditionalRedirect(false);

            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader(LOCATION_HEADER, "/eg043/listEnvelopes");
            return null;
        }

        if (user.getAccessToken() != null && config.getSelectedApiType().equals(ApiType.ESIGNATURE)) {
            try {
                isCFR = Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId());
            } catch (Exception exception) {
                return exception.toString();
            }
        }

        if (config.getQuickstart().equals("true") && config.getSelectedApiIndex().equals(ApiIndex.ESIGNATURE) &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken)) {
            String site = ApiIndex.ESIGNATURE.getPathOfFirstExample();
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader(LOCATION_HEADER, site);
            return null;
        }

        if (isCFR) {
            session.setStatusCFR("enabled");
            model.addAttribute(STATUS_CFR, "enabled");
        }
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(CODE_EXAMPLE_GROUPS, config.getCodeExamplesText().APIs.toArray());
        model.addAttribute(API_DATA, config.loadFileData(config.getCodeExamplesManifest()));
        return session.getApiIndexPath();
    }

    @GetMapping(path = "/ds/mustAuthenticate")
    public ModelAndView mustAuthenticateController(ModelMap model, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(ATTR_TITLE, config.getCodeExamplesText().SupportingTexts.LoginPage.LoginButton);

        String redirectURL = getRedirectURLForJWTAuthentication(req, resp);

        if (config.getIsConsentRedirectActivated()) {
            config.setIsConsentRedirectActivated(false);
            this.session.setAuthTypeSelected(AuthType.JWT);

            return new ModelAndView(
                    new JWTAuthenticationMethod().loginUsingJWT(config, session, redirectURL, ApiType.getScopeList()));
        }

        boolean isRedirectToMonitor = redirectURL.toLowerCase().contains("/m") &&
                !redirectURL.toLowerCase().contains("/mae");
        if (session.isRefreshToken() || config.getQuickstart().equals("true")) {
            config.setQuickstart("false");

            if (isRedirectToMonitor || session.getMonitorExampleRedirect() != null) {
                return checkForMonitorRedirects(redirectURL);
            }

            return new ModelAndView(getRedirectView(session.getAuthTypeSelected()));
        } else if (isRedirectToMonitor || session.getMonitorExampleRedirect() != null) {
            return checkForMonitorRedirects(redirectURL);
        } else {
            return new ModelAndView("pages/ds_must_authenticate");
        }
    }

    private ModelAndView checkForMonitorRedirects(String redirectURL) {
        this.session.setAuthTypeSelected(AuthType.JWT);
        redirectURL = redirectURL != "/" ? redirectURL : session.getMonitorExampleRedirect();
        this.session.setMonitorExampleRedirect(null);
        return new ModelAndView(
                new JWTAuthenticationMethod().loginUsingJWT(config, session, redirectURL, ApiType.getScopeList()));
    }

    @GetMapping("/pkce")
    public RedirectView pkce(String code, String state, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String redirectURL = getRedirectURLForJWTAuthentication(req, resp);
        RedirectView redirect;
        try {
            redirect = new ACGAuthenticationMethod().exchangeCodeForToken(code, config, session, redirectURL,
                    ApiType.getScopeList());
        } catch (Exception e) {
            redirect = getRedirectView(AuthType.AGC);
            this.session.setIsPKCEWorking(false);
        }

        return redirect;
    }

    @PostMapping("/ds/authenticate")
    public RedirectView authenticate(ModelMap model, @RequestBody MultiValueMap<String, String> formParams,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!formParams.containsKey("selectAuthType")) {
            model.addAttribute("message", "Select option with selectAuthType name must be provided.");
            return new RedirectView("pages/error");
        }

        String redirectURL = getRedirectURLForJWTAuthentication(req, resp);

        List<String> selectAuthTypeObject = formParams.get("selectAuthType");
        AuthType authTypeSelected = AuthType.valueOf(selectAuthTypeObject.get(0));

        if (authTypeSelected.equals(AuthType.JWT)) {
            this.session.setAuthTypeSelected(AuthType.JWT);
            return new JWTAuthenticationMethod().loginUsingJWT(config, session, redirectURL, ApiType.getScopeList());
        } else {
            this.session.setAuthTypeSelected(AuthType.AGC);
            if (this.session.getIsPKCEWorking()) {
                return new ACGAuthenticationMethod().initiateAuthorization(config, ApiType.getScopeList());
            } else {
                return getRedirectView(authTypeSelected);
            }
        }
    }

    private String getRedirectURLForJWTAuthentication(HttpServletRequest req, HttpServletResponse resp) {
        SavedRequest savedRequest = requestCache.getRequest(req, resp);

        String[] examplesCodes = new String[] {
                ApiIndex.CLICK.getExamplesPathCode(),
                ApiIndex.ESIGNATURE.getExamplesPathCode(),
                ApiIndex.MONITOR.getExamplesPathCode(),
                ApiIndex.ADMIN.getExamplesPathCode(),
                ApiIndex.ROOMS.getExamplesPathCode(),
        };

        if (savedRequest != null) {
            Integer indexOfExampleCodeInRedirect = StringUtils.indexOfAny(savedRequest.getRedirectUrl(), examplesCodes);

            if (indexOfExampleCodeInRedirect != -1) {
                Boolean hasNumbers = savedRequest.getRedirectUrl().substring(indexOfExampleCodeInRedirect)
                        .matches(".*\\d.*");

                return "GET".equals(savedRequest.getMethod()) && hasNumbers ? savedRequest.getRedirectUrl() : "/";
            }
        }

        return "/";
    }

    @GetMapping(path = "/ds-return")
    public String returnController(@RequestParam(value = ATTR_STATE, required = false) String state,
            @RequestParam(value = ATTR_EVENT, required = false) String event,
            @RequestParam(required = false) String envelopeId, ModelMap model) {
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(ATTR_TITLE, "Return from DocuSign");
        model.addAttribute(ATTR_EVENT, event);
        model.addAttribute(ATTR_STATE, state);
        model.addAttribute(ATTR_ENVELOPE_ID, envelopeId);
        return "pages/ds_return";
    }

    private RedirectView getRedirectView(AuthType authTypeSelected) {
        session.setAuthTypeSelected(authTypeSelected);
        RedirectView redirect = new RedirectView(getLoginPath(authTypeSelected));
        redirect.setExposeModelAttributes(false);
        return redirect;
    }

    private String getLoginPath(AuthType authTypeSelected) {
        String loginPath = config.getAcgRedirectURL();
        if (authTypeSelected.equals(AuthType.JWT)) {
            loginPath = config.getJwtRedirectURL();
        }
        return loginPath;
    }
}
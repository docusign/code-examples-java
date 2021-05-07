package com.docusign.core.controller;

import com.docusign.DSConfiguration;
import com.docusign.core.model.AuthType;
import com.docusign.core.model.Session;
import com.docusign.core.security.OAuthProperties;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexController {
    private static final String ATTR_ENVELOPE_ID = "qpEnvelopeId";
    private static final String ATTR_STATE = "state";
    private static final String ATTR_EVENT = "event";
    private static final String ATTR_TITLE = "title";

    @Autowired
    private Session session;

    @Autowired
    private OAuthProperties jwtGrantSso;

    @Autowired
    private OAuthProperties authCodeGrantSso;

    @Autowired
    private DSConfiguration config;

    @GetMapping(path = "/")
    public String index(ModelMap model) {
        model.addAttribute(ATTR_TITLE,"Home");
        return session.getApiIndexPath();
    }

    @GetMapping(path = "/ds/mustAuthenticate")
    public ModelAndView mustAuthenticateController(ModelMap model) {
        model.addAttribute(ATTR_TITLE, "Authenticate with DocuSign");
        if (session.isRefreshToken() || config.getQuickstart().equals("true")) {
            return new ModelAndView(getRedirectView(session.getAuthTypeSelected()));
        }
        else if (config.getApiName().equals("MONITOR")) {
            return new ModelAndView(getRedirectView(AuthType.JWT));
        }
        else {
            return new ModelAndView("pages/ds_must_authenticate");
        }
    }

    @RequestMapping(path = "/ds/authenticate", method = RequestMethod.POST)
    public RedirectView authenticate(ModelMap model, @RequestBody MultiValueMap<String, String> formParams) {
        if (!formParams.containsKey("selectAuthType")) {
            model.addAttribute("message", "select option with selectAuthType name must be provided");
            return new RedirectView("pages/error");
        }
        List<String> selectAuthTypeObject = formParams.get("selectAuthType");
        AuthType authTypeSelected = AuthType.valueOf(selectAuthTypeObject.get(0));
        return getRedirectView(authTypeSelected);
    }

    @GetMapping(path = "/ds-return")
    public String returnController(@RequestParam(value = ATTR_STATE, required = false) String state,
            @RequestParam(value = ATTR_EVENT, required = false) String event,
            @RequestParam(value = "envelopeId", required = false) String envelopeId, ModelMap model) {
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
        OAuthProperties oAuth2SsoProperties = authCodeGrantSso;
        if (authTypeSelected.equals(AuthType.JWT)) {
            oAuth2SsoProperties = jwtGrantSso;
        }
        return oAuth2SsoProperties.getLoginPath();
    }
}

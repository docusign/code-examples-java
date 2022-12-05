package com.docusign.core.controller;

import com.docusign.DSConfiguration;
import com.docusign.common.ApiIndex;
import com.docusign.core.common.Utils;
import com.docusign.core.model.ApiType;
import com.docusign.core.model.AuthType;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.core.security.OAuthProperties;
import com.docusign.core.security.jwt.JWTAuthenticationMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

@Controller
public class IndexController {
    private static final String ATTR_ENVELOPE_ID = "qpEnvelopeId";

    private static final String ATTR_STATE = "state";

    private static final String ATTR_EVENT = "event";

    private static final String ATTR_TITLE = "title";

    private static final String LAUNCHER_TEXTS = "launcherTexts";

    private static final String CODE_EXAMPLE_GROUPS = "codeExampleGroups";

    private static final String STATUS_CFR = "statusCFR";

    @Autowired
    private Session session;

    @Autowired
    private User user;

    @Autowired
    private RequestCache requestCache;

    @Autowired
    private OAuthProperties jwtGrantSso;

    @Autowired
    private OAuthProperties authCodeGrantSso;

    @Autowired
    private DSConfiguration config;

    @GetMapping(path = "/")
    public String index(ModelMap model, HttpServletResponse response) throws IOException {
        model.addAttribute(ATTR_TITLE, "Home");

        Boolean isCFR = false;

        if (user.getAccessToken() != null && config.getSelectedApiType().equals(ApiType.ESIGNATURE)) {
            try {
                isCFR = Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId());
            } catch (Exception exception) {
                return exception.toString();
            }
        }

        if (config.getQuickstart().equals("true") && config.getSelectedApiIndex().equals(ApiIndex.ESIGNATURE) &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken)) {
            String site = config.getSelectedApiIndex().getPathOfFirstExample();
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", site);
            return null;
        }

        if (isCFR) {
            session.setStatusCFR("enabled");
            model.addAttribute(STATUS_CFR, "enabled");
        }
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(CODE_EXAMPLE_GROUPS, config.getCodeExamplesText().Groups.toArray());
        return session.getApiIndexPath();
    }

    @GetMapping(path = "/ds/mustAuthenticate")
    public ModelAndView mustAuthenticateController(ModelMap model, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(ATTR_TITLE, config.getCodeExamplesText().SupportingTexts.LoginPage.LoginButton);

        SavedRequest savedRequest = requestCache.getRequest(req, resp);
        var redirectURL = savedRequest != null && "GET".equals(savedRequest.getMethod()) ?
                        savedRequest.getRedirectUrl() : "/";

        if (config.getIsConsentRedirectActivated()) {
            config.setIsConsentRedirectActivated(false);
            this.session.setAuthTypeSelected(AuthType.JWT);

            return new ModelAndView(JWTAuthenticationMethod.loginUsingJWT(
                    config.getSelectedApiType(),
                    config.getUserId(),
                    config.getImpersonatedUserId(),
                    config.getBaseURL(),
                    config,
                    "/"));
        }

        if (session.isRefreshToken() || config.getQuickstart().equals("true")) {
            config.setQuickstart("false");

            if (config.getSelectedApiType().equals(ApiType.MONITOR)) {
                this.session.setAuthTypeSelected(AuthType.JWT);

                return new ModelAndView(JWTAuthenticationMethod.loginUsingJWT(
                        config.getSelectedApiType(),
                        config.getUserId(),
                        config.getImpersonatedUserId(),
                        config.getBaseURL(),
                        config,
                        redirectURL));
            }

            return new ModelAndView(getRedirectView(session.getAuthTypeSelected()));
        } else if (config.getSelectedApiType().equals(ApiType.MONITOR)) {
            this.session.setAuthTypeSelected(AuthType.JWT);

            return new ModelAndView(JWTAuthenticationMethod.loginUsingJWT(
                    config.getSelectedApiType(),
                    config.getUserId(),
                    config.getImpersonatedUserId(),
                    config.getBaseURL(),
                    config,
                    redirectURL));
        } else {
            return new ModelAndView("pages/ds_must_authenticate");
        }
    }

    @GetMapping(path = "/ds/selectApi")
    public Object choseApiType(ModelMap model) {
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        return new ModelAndView("pages/ds_select_api");
    }

    @RequestMapping(path = "/ds/selectApi", method = RequestMethod.POST)
    public Object getApiType(ModelMap model, @RequestBody MultiValueMap<String, String> formParams) throws IOException, URISyntaxException {
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);

        if (!formParams.containsKey("selectApiType")) {
            model.addAttribute("message", "Select option with selectApiType name must be provided.");
            return new RedirectView("pages/error");
        }
        List<String> selectApiTypeObject = formParams.get("selectApiType");
        ApiType apiTypeSelected = ApiType.valueOf(selectApiTypeObject.get(0));
        writeApiTypeIntoFile(apiTypeSelected);
        writeCorrectScopesIntoApplication(apiTypeSelected);

        return new ModelAndView("pages/ds_restart");
    }

    private void writeApiTypeIntoFile(ApiType apiTypeSelected) throws URISyntaxException, IOException {
        JSONObject currentApiType = new JSONObject();
        currentApiType.put(config.getApiTypeHeader(), apiTypeSelected.name());

        Path exampleApiSourcePath = Paths.get("").resolve("src").resolve("main")
                .resolve("resources").resolve(config.getExamplesApiPath());
        if (Files.exists(exampleApiSourcePath)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(exampleApiSourcePath.toAbsolutePath().toString()))
            ) {
                bufferedWriter.write(currentApiType.toString());
            }
        } else {
            // Works in case we create war and run it on 'tomcat' server
            URL exampleApiURL = getClass().getClassLoader().getResource(config.getExamplesApiPath());
            try (BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(new File(Objects.requireNonNull(exampleApiURL).toURI())))
            ) {
                bufferedWriter.write(currentApiType.toString());
            }
        }
    }

    private void writeCorrectScopesIntoApplication(ApiType apiTypeSelected) throws IOException {
        Path applicationJsonSourcePath = Paths.get("").resolve("src").resolve("main")
                .resolve("resources").resolve(config.getConfigFilePath());

        List lines = Files.readAllLines(applicationJsonSourcePath, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder(1024);
        for (Object line : lines) {
            stringBuilder.append(line);
        }
        String originalJsonValueString = stringBuilder.toString();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        JsonObject originalJsonValue = gson.fromJson(originalJsonValueString, JsonObject.class);
        JsonObject acgConfigurationValues = originalJsonValue.getAsJsonObject("spring")
                .getAsJsonObject("security")
                .getAsJsonObject("oauth2")
                .getAsJsonObject("client")
                .getAsJsonObject("registration")
                .getAsJsonObject("acg")
                .getAsJsonObject();

        acgConfigurationValues.addProperty(
                "scope",
                String.join(", ", apiTypeSelected.getScopes())
        );

        Files.write(
                applicationJsonSourcePath,
                gson.toJson(originalJsonValue).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    @RequestMapping(path = "/ds/authenticate", method = RequestMethod.POST)
    public RedirectView authenticate(ModelMap model, @RequestBody MultiValueMap<String, String> formParams, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!formParams.containsKey("selectAuthType")) {
            model.addAttribute("message", "Select option with selectAuthType name must be provided.");
            return new RedirectView("pages/error");
        }

        SavedRequest savedRequest = requestCache.getRequest(req, resp);
        var redirectURL = savedRequest != null && "GET".equals(savedRequest.getMethod()) ?
                savedRequest.getRedirectUrl() : "/";

        List<String> selectAuthTypeObject = formParams.get("selectAuthType");
        AuthType authTypeSelected = AuthType.valueOf(selectAuthTypeObject.get(0));

        if (authTypeSelected.equals(AuthType.JWT)){
            this.session.setAuthTypeSelected(AuthType.JWT);
            return JWTAuthenticationMethod.loginUsingJWT(
                    config.getSelectedApiType(),
                    config.getUserId(),
                    config.getImpersonatedUserId(),
                    config.getBaseURL(),
                    config,
                    redirectURL);
        }else {
            return getRedirectView(authTypeSelected);
        }
    }

    @GetMapping(path = "/ds-return")
    public String returnController(@RequestParam(value = ATTR_STATE, required = false) String state,
                                   @RequestParam(value = ATTR_EVENT, required = false) String event,
                                   @RequestParam(value = "envelopeId", required = false) String envelopeId, ModelMap model) {
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
        OAuthProperties oAuth2SsoProperties = authCodeGrantSso;
        if (authTypeSelected.equals(AuthType.JWT)) {
            oAuth2SsoProperties = jwtGrantSso;
        }
        return oAuth2SsoProperties.getLoginPath();
    }
}

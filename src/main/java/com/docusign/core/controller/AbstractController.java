package com.docusign.core.controller;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;

import com.docusign.core.model.manifestModels.CodeExampleText;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Objects;


/**
 * Abstract base class for all controllers. It handles all requests which are
 * registered by the derived classes and delegates an example specific action
 * back to the appropriate controllers. If a user had not been authorized, it
 * redirects him onto an authentication page. Derived classes must override an
 * abstract method {@link #doWork(WorkArguments, ModelMap, HttpServletResponse)}
 * to do something. This method is called from the POST request. Optionally you
 * can override method {@link #onInitModel(WorkArguments, ModelMap)} to add
 * example specific attributes into a page.
 */
@Controller
public abstract class AbstractController {

    private static final String REDIRECT_PREFIX = "redirect:";
    protected static final String REDIRECT_SELECT_API_PAGE = REDIRECT_PREFIX + "/ds/selectApi";
    protected static final String BEARER_AUTHENTICATION = "Bearer ";
    protected static final String DONE_EXAMPLE_PAGE = "pages/example_done";
    protected static final String DONE_EXAMPLE_PAGE_COMPARE = "pages/example_done_compare";
    protected static final String EXAMPLE_PENDING_PAGE = "pages/example_pending";
    protected static final String ERROR_PAGE = "error";
    private static final String EXAMPLE_TEXT = "example";
    protected static final String LAUNCHER_TEXTS = "launcherTexts";
    protected static final String REDIRECT_CFR_QUICKSTART = REDIRECT_PREFIX + "/eg041";

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    protected Session session;

    protected final String exampleName;
    private CodeExampleText codeExampleText;
    protected String title;
    private final String pagePath;
    protected final DSConfiguration config;

    public AbstractController(DSConfiguration config, String exampleName) {
        this.config = config;
        this.exampleName = exampleName;
        this.pagePath = this.getExamplePagesPath() + exampleName;
    }

    public CodeExampleText getTextForCodeExample() {
        if (codeExampleText != null) {
            return codeExampleText;
        }

        codeExampleText = GetExampleText();

        return codeExampleText;
    }

    protected abstract String getExamplePagesPath();

    @GetMapping
    public String get(WorkArguments args, ModelMap model) {
        if (isTokenExpired()) {
            return REDIRECT_SELECT_API_PAGE;
        }

        try {
            onInitModel(args, model);
            return pagePath;
        } catch (Exception exception) {
            if (config.getQuickstart().equals("true") && exception.getMessage() == config.getCodeExamplesText().getSupportingTexts().getCFRError()){
              config.setQuickstart("false");
              return handleRedirectToCfr(model);
            } else {
              return handleException(exception, model);
            }
        }
    }

    @PostMapping
    public Object create(WorkArguments args, ModelMap model, HttpServletResponse response) {
        if (isTokenExpired()) {
            return REDIRECT_SELECT_API_PAGE;
        }

        try {
            return doWork(args, model, response);
        } catch (Exception exception) {
            return handleException(exception, model);
        }
    }

    /**
     * This method is called from the GET request and it should initialize a
     * model. Override this method if it is necessary to add example specific
     * attributes into the model.
     * @param args the session attributes
     * @param model the model data
     * @throws Exception if calling API has failed
     */
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        this.title = getTextForCodeExample().ExampleName;

        Class<?> clazz = Objects.requireNonNullElse(getClass().getEnclosingClass(), getClass());
        String srcPath = String.join("", config.getExampleUrl(), clazz.getName().replace('.', '/'), ".java");
        String viewSourceFile = config.getCodeExamplesText().SupportingTexts
                .getViewSourceFile().replaceFirst(
                        "\\{0}",
                        "<a target='_blank' href='" + srcPath + "'>" + clazz.getSimpleName() + ".java" + "</a>"
                );
        model.addAttribute("csrfToken", "");
        model.addAttribute("title", title);
        model.addAttribute("viewSourceFile", viewSourceFile);
        model.addAttribute("documentation", config.getDocumentationPath() + exampleName);
        model.addAttribute(EXAMPLE_TEXT, getTextForCodeExample());
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
    }

    /**
     * This method is called from the POST request. Example pages must override
     * it. Overridden method should return an Object means URL to redirect.
     * @param args the work arguments got from the current page
     * @param model page model holder
     * @param response for HTTP-specific functionality in sending a response
     * @return {@link Object}. Possible types and values: <code>null</code>,
     * {@link String} representation of the URL or Spring RedirectView object,
     * (see {@link org.springframework.web.servlet.view.RedirectView RedirectView})
     * @throws Exception if calling API has failed or if I/O operation has failed
     */
    protected abstract Object doWork(WorkArguments args, ModelMap model,
        HttpServletResponse response) throws Exception;

    private String handleException(Exception exception, ModelMap model) {
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        String exceptionMessage = "";
        String fixingInstructions = "";

        if (exception != null) {
            exceptionMessage = exception.getMessage();
            if(model.getAttribute("caseForInstructions") != null) {
                fixingInstructions = exceptionMessage.contains((CharSequence) model.getAttribute("caseForInstructions")) ?
                        (String) model.getAttribute("fixingInstructions") : null;
            }
        }
        new DoneExample()
            .withTitle(exampleName)
            .withName(title)
            .withMessage(exceptionMessage)
            .withFixingInstructions(fixingInstructions)
            .withStackTracePrinted(stackTrace)
            .addToModel(model, config);
        return ERROR_PAGE;
    }

    private String handleRedirectToCfr(ModelMap model) {
      return REDIRECT_CFR_QUICKSTART;
    }

    private boolean isTokenExpired() {
        OAuth2AccessToken accessToken = oAuth2ClientContext.getAccessToken();
        boolean tokenExpired = accessToken != null && accessToken.isExpired();
        session.setRefreshToken(tokenExpired);
        return tokenExpired;
    }


    protected CodeExampleText GetExampleText() {
        var groups = config.getCodeExamplesText().Groups;
        var exampleNumberToSearch =  Integer.parseInt(this.exampleName.replaceAll("\\D+", ""));

        for(var i = 0; i < groups.size(); ++i)
        {
            CodeExampleText codeExampleText = (CodeExampleText) Arrays
                    .stream(groups.get(i).Examples.toArray())
                    .filter(x -> ((CodeExampleText) x).ExampleNumber == exampleNumberToSearch)
                    .findFirst()
                    .orElse(null);

            if (codeExampleText != null)
            {
                return codeExampleText;
            }
        }
        return null;
    }
}

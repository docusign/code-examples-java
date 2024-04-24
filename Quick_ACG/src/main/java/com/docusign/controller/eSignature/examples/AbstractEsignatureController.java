package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.User;
import com.docusign.core.model.manifestModels.APIs;
import com.docusign.core.model.manifestModels.CodeExampleText;
import com.docusign.core.model.Session;
import com.docusign.core.model.manifestModels.ManifestGroup;
import com.docusign.esign.client.ApiClient;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all eSignature controllers.
 */
@Controller
public abstract class AbstractEsignatureController {

    protected static final String BEARER_AUTHENTICATION = "Bearer ";

    @Autowired
    protected Session session;

    protected final String exampleName;
    protected String title;
    private final String pagePath;
    protected final DSConfiguration config;
    protected CodeExampleText codeExampleText;
    private static final String EXAMPLE_PAGES_PATH = "pages/esignature/examples/";
    private static final String EXAMPLE_TEXT = "example";
    private static final String LAUNCHER_TEXTS = "launcherTexts";
    protected final User user;

    public AbstractEsignatureController(DSConfiguration config, String exampleName, Session session, User user) {
        this.config = config;
        this.session = session;
        this.user = user;
        this.exampleName = exampleName;
        this.pagePath = this.getExamplePagesPath() + exampleName;
    }

    protected String getExamplePagesPath() {
        return AbstractEsignatureController.EXAMPLE_PAGES_PATH;
    }

    /**
     * Creates new instance of the eSignature API client.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        return apiClient;
    }

    @GetMapping
    public String get(WorkArguments args, ModelMap model) {
        try {
            onInitModel(args, model);
            return pagePath;
        } catch (Exception exception) {
            return handleException(exception, model);
        }
    }

    @PostMapping
    public Object create(WorkArguments args, ModelMap model, HttpServletResponse response) {
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
     * @param args
     * @param model the model data
     */
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        this.codeExampleText = GetExampleText();
        this.title = this.codeExampleText.ExampleName;

        Class<?> clazz = Objects.requireNonNullElse(getClass().getEnclosingClass(), getClass());
        String srcPath = String.join("", config.getExampleUrl(), clazz.getName().replace('.', '/'), ".java");
        model.addAttribute("csrfToken", "");
        model.addAttribute("title", title);
        model.addAttribute("sourceFile", clazz.getSimpleName() + ".java");
        model.addAttribute("sourceUrl", srcPath);
        model.addAttribute("documentation", config.getDocumentationPath() + exampleName);
        model.addAttribute(EXAMPLE_TEXT, this.codeExampleText);
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
        return ExceptionUtils.getStackTrace(exception);
    }

    protected CodeExampleText GetExampleText() {
        List<ManifestGroup> eSignatureManifestGroups = config.getCodeExamplesText().APIs.stream()
                .filter(x -> "esignature".contains(((APIs) x).Name.toLowerCase()))
                .findFirst()
                .orElse(null).Groups;
        var exampleNumberToSearch = 1;

        for (ManifestGroup group : eSignatureManifestGroups) {
            CodeExampleText codeExampleText = group.Examples.stream()
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

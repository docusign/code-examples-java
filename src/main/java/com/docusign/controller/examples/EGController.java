package com.docusign.controller.examples;

import com.docusign.DSConfiguration;
import com.docusign.esign.client.ApiException;
import com.docusign.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public abstract class EGController {
    private final static int minimumBufferMin = 3;

    private String message;

    @Autowired
    protected HttpSession session;

    @Autowired
    protected DSConfiguration config;

    @Autowired
    User user;

    // Base method for the example GET requests which show the example's
    // form page
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model, HttpSession session, HttpServletRequest request) {
        // Check that the token is valid and will remain valid for awhile to enable the
        // user to fill out the form. If the token is not available, now is the time
        // to have the user authenticate or re-authenticate.
        boolean tokenOk = checkToken();
        model.addAttribute("showDoc", config.documentation != null);
        if (tokenOk) {
            model.addAttribute("csrfToken", "");
            model.addAttribute("title", getTitle());
            addSpecialAttributes(model);
            model.addAttribute("source", createSourcePath());
            model.addAttribute("documentation", config.documentation + getEgName());
            model.addAttribute("showDoc", config.documentation != null);

            return "pages/examples/" + getEgName();
        }

        session.setAttribute("eg", getEgName());
        return "redirect:/ds/mustAuthenticate";

    }

    protected abstract void addSpecialAttributes(ModelMap model);

    // Base method for POST requests to run an example
    @RequestMapping(method = RequestMethod.POST)
    public Object create(WorkArguments args,
                         ModelMap model,
                         HttpSession session,
                         @RequestBody MultiValueMap<String, String> formParams,
                         HttpServletResponse response,
                         @ModelAttribute("accessToken") String accessToken,
                         @ModelAttribute("basePath") String basePath) throws IOException, ApiException {
        // Check again that we have a token. Only a minimal token time is
        // needed since we are about to call DocuSign
        boolean tokenOk = checkToken(minimumBufferMin);
        if (!tokenOk) {
            session.setAttribute("eg", getEgName());
            return "redirect:/ds/mustAuthenticate";
        }

        try {
            loadFromSessionOrBody(args, formParams);
            Object result = doWork(args, model, accessToken, basePath);
            String redirectUrl = args.getRedirectUrl();
            Boolean externalRedirect = redirectUrl != null && redirectUrl.indexOf("redirect:") == 0;
            if (externalRedirect) {
                String url = redirectUrl.substring(9); // strip 'redirect:'
                RedirectView redirect = new RedirectView(url);
                redirect.setExposeModelAttributes(false);
                return redirect;
            } else if (redirectUrl != null) {
                // show a generic template
                postWork(result, model);
                return redirectUrl;
            } else {
                // download logic
                JSONObject r = (JSONObject) result;

                byte[] buffer = (byte[]) r.get("fileBytes");

                response.setContentType(r.getString("mimetype"));
                response.setContentLength(buffer.length);
                response.setHeader("Content-disposition", "inline;filename=" + r.getString("docName"));

                response.getOutputStream().write(buffer);
                response.flushBuffer();
                return null;
            }

        } catch (Exception e) {
            populateErrorModel(model, e);
            throw new RuntimeException(e);
        }
    }

    private void loadFromSessionOrBody(WorkArguments args, MultiValueMap<String, String> formParams) {
        args.setAccountId((String) session.getAttribute("accountId"));
        args.setEnvelopeId((String) session.getAttribute("envelopeId"));
        args.setTemplateId((String) session.getAttribute("templateId"));
        if (formParams.containsKey("docSelect")) {
            args.setDocumentId(formParams.getFirst("docSelect"));
        } else if (formParams.containsKey("startingView")) {
            args.setStartingView(formParams.getFirst("startingView"));
        } else if (formParams.containsKey("item")) {
            args.setItem(formParams.getFirst("item"));
        } else if (formParams.containsKey("quantity")) {
            args.setQuantity(formParams.getFirst("quantity"));
        }
        args.setEnvelopeDocuments((JSONObject) session.getAttribute("envelopeDocuments"));
    }

    protected void postWork(Object result, ModelMap model) {

        String title = getResponseTitle();
        String message = getMessage();
        model.addAttribute("title", title);
        model.addAttribute("h1", title);
        model.addAttribute("message", message);

        model.addAttribute("json",
                (result != null) ? new JSONObject(result).toString(4) : null);
    }


    protected void populateErrorModel(ModelMap model, Exception e) {
        model.addAttribute("err", e);
        model.addAttribute("errorCode", e.getCause());
        model.addAttribute("errorMessage", e.getMessage());
    }

    private boolean checkToken() {
        return checkToken(60);
    }

    // Check that we have an Access Token and for how long it will last
    private boolean checkToken(int minimumBufferMin) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (!(authentication instanceof OAuth2Authentication)) {
            return false;
        }

        OAuth2Authentication oauth = (OAuth2Authentication) authentication;
        return oauth.getUserAuthentication().isAuthenticated();

        // At this point we'd like to check the expiration time of the token
        // vs the minimumBufferMin argument. But it is not clear how
        // to obtain the expiration time from Spring Security OAuth client library.
        // See https://stackoverflow.com/questions/52432735/spring-security-oauth2-client-access-the-expires-in-value
        //
        // Since DocuSign tokens granted via the Authorization Code Grant flow
        // normally have an 8 hour life, this issue can be punted for now since
        // the default Spring Session time is 30 min.

//        return (user.accessToken != null
//                || user.tokenExpirationTimestamp - (minimumBufferMin*60*1000) > System.currentTimeMillis());
    }

    protected abstract String getEgName();

    protected abstract String getTitle();

    protected abstract String getResponseTitle();

    protected String createSourcePath() {
        String source;
        Class<?> enclosingClass = getClass().getEnclosingClass();
        if (enclosingClass != null) {
            source = enclosingClass.getName();
        } else {
            source = getClass().getName();
        }

        source = source.replace('.', '/');
        return config.githubExampleUrl + source + ".java";
    }

    protected byte[] readFile(String path) throws IOException {
        InputStream is = EGController.class.getResourceAsStream("/" + path);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;

        byte[] data = new byte[1024];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    protected abstract Object doWork(WorkArguments args, ModelMap model,
                                     @ModelAttribute("accessToken") String accessToken,
                                     @ModelAttribute("basePath") String basePath) throws ApiException, IOException;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

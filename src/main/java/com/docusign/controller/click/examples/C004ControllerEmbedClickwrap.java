package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Embed a clickwrap.
 */
@Controller
@RequestMapping("/c004")
public class C004ControllerEmbedClickwrap extends AbstractClickController {

    private static final String MODEL_CLICKWRAP_OK = "clickwrapOk";
    private static final String MODEL_CLICKWRAP_ID = "clickwrapId";

    private final Session session;
    private final User user;


    @Autowired
    public C004ControllerEmbedClickwrap(DSConfiguration config, Session session, User user) {
        super(config, "c004", "Embed a clickwrap");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        boolean isClickwrapOk = this.isClickwrapExistsAndActive(
                this.session.getBasePath(),
                this.user.getAccessToken(),
                this.session.getAccountId(),
                this.session.getClickwrapId());

        model.addAttribute(MODEL_CLICKWRAP_OK, isClickwrapOk);
        model.addAttribute(MODEL_CLICKWRAP_ID, this.session.getClickwrapId());
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) {
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}

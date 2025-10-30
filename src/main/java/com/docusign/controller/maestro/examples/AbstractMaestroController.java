package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.iam.sdk.IamClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Maestro fields controllers.
 */
@Controller
public abstract class AbstractMaestroController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/maestro/examples/";

    protected final User user;

    protected final Session session;

    public AbstractMaestroController(DSConfiguration config, String exampleName, User user, Session session) {
        super(config, exampleName);
        this.user = user;
        this.session = session;
    }

    //ds-snippet-start:MaestroJavaStep2
    protected IamClient createAuthenticatedClient(String accessToken) {
        return IamClient.builder()
                .accessToken(accessToken)
                .build();
    }
    //ds-snippet-end:MaestroJavaStep2

    protected String serializeObjectToJson(Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());

        return mapper.writeValueAsString(data);
    }

    protected String getExamplePagesPath() {
        return AbstractMaestroController.EXAMPLE_PAGES_PATH;
    }
}

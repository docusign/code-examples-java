package com.docusign.controller.connect.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.connect.services.ValidateUsingHmacService;
import com.docusign.core.model.DoneExample;
import com.docusign.esign.client.ApiException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/con001")
public class EG001ControllerValidateUsingHMAC extends AbstractConnectController {

    public EG001ControllerValidateUsingHMAC(DSConfiguration config) {
        super(config, "con001");
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        String hashedOutput = new ValidateUsingHmacService().computeHash(
            args.getHmacSecret(),
            args.getJsonPayload().getBytes()
        );

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
            .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                .replaceFirst("\\{0}", hashedOutput)
            )
            .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}

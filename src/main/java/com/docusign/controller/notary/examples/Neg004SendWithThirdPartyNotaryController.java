package com.docusign.controller.notary.examples;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.controller.notary.services.SendWithThirdPartyNotaryService;

@Controller
@RequestMapping("/n004")
public class Neg004SendWithThirdPartyNotaryController extends AbstractNotaryController {

	public Neg004SendWithThirdPartyNotaryController(DSConfiguration config, Session session, User user) {
		super(config, "n004", session, user);
	}

	@Override
	protected Object doWork(
			WorkArguments args,
			ModelMap model,
			HttpServletResponse response)
			throws ApiException, IOException, com.docusign.webforms.client.ApiException {
		EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

		// Call the Examples API method to create and send an notary envelope via email
		var envelopeId = SendWithThirdPartyNotaryService.sendWithNotary(
				args.getSignerEmail(),
				args.getSignerName(),
				this.session.getAccountId(),
				envelopesApi,
				args);

		DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
				.withMessage(getTextForCodeExampleByApiType().ResultsPageText
						.replaceFirst("\\{0}", envelopeId))
				.addToModel(model, config);
		return DONE_EXAMPLE_PAGE;
	}
}

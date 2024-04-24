package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.GetRoomsWithFiltersService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.core.utils.DateUtils;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoomSummaryList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Get Rooms with filters.
 */
@Controller
@RequestMapping("/r005")
public class R005ControllerGetRoomsWithFilters extends AbstractRoomsController {

    private static final String MODEL_START_DATE = "startDate";

    private static final String MODEL_END_DATE = "endDate";

    private static final int FROM_DATE_OFFSET_DAYS = 10;

    private static final int FROM_DATE_FORWARD_DAYS = 1;

    public R005ControllerGetRoomsWithFilters(DSConfiguration config, Session session, User user) {
        super(config, "r005");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        LocalDateTime current = LocalDateTime.now().plusDays(FROM_DATE_FORWARD_DAYS);
        LocalDateTime past = LocalDateTime.now().minusDays(FROM_DATE_OFFSET_DAYS);

        model.addAttribute(MODEL_START_DATE, DateUtils.DATE_WITH_LINES.format(past));
        model.addAttribute(MODEL_END_DATE, DateUtils.DATE_WITH_LINES.format(current));
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2: Construct your API headers
        //ds-snippet-start:Rooms5Step2
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        //ds-snippet-end:Rooms5Step2

        // Step 4. Call the v2 Rooms API
        //ds-snippet-start:Rooms5Step4
        RoomSummaryList rooms = GetRoomsWithFiltersService.getRoomsWithFilters(
                roomsApi,
                this.session.getAccountId(),
                args.getStartDate(),
                args.getEndDate());
        //ds-snippet-end:Rooms5Step4

        DoneExample.createDefault(this.title)
                .withJsonObject(rooms)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}

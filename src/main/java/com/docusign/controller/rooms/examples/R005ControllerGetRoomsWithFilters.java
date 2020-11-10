package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.RoomTemplatesApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoomSummaryList;
import com.docusign.rooms.model.RoomTemplatesSummaryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Session session;
    private final User user;


    @Autowired
    public R005ControllerGetRoomsWithFilters(DSConfiguration config, Session session, User user) {
        super(config, "r005", "Get Rooms with filters");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        LocalDateTime current = LocalDateTime.now().plusDays(FROM_DATE_FORWARD_DAYS);
        LocalDateTime past = LocalDateTime.now().minusDays(FROM_DATE_OFFSET_DAYS);

        model.addAttribute(MODEL_START_DATE, DATE_FORMATTER.format(past));
        model.addAttribute(MODEL_END_DATE, DATE_FORMATTER.format(current));
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2: Construct your API headers
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3. Prepare your request parameters
        RoomsApi.GetRoomsOptions options = roomsApi.new GetRoomsOptions();
        options.setFieldDataChangedStartDate(args.getStartDate());
        options.setFieldDataChangedEndDate(args.getEndDate());

        // Step 4. Call the v2 Rooms API
        RoomSummaryList rooms = roomsApi.getRooms(this.session.getAccountId(), options);

        DoneExample.createDefault(this.title)
                .withJsonObject(rooms)
                .withMessage("Rooms has been filtered and returned!")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}

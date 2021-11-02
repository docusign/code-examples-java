package com.services.admin;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.OrganizationUserResponse;
import com.docusign.admin.model.OrganizationUsersResponse;
import com.docusign.admin.model.UserDrilldownResponse;
import com.docusign.admin.model.UsersDrilldownResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class AuditUsersService {
    private static final String timeZoneId = "UTC";
    private static final String simpleDateFormat = "yyyy-MM-dd'T'HH:mm'Z'";

    public static ArrayList<UserDrilldownResponse>  auditUsers(
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId
    ) throws ApiException {
        ArrayList<UserDrilldownResponse> resultsArr = new ArrayList<>();

        // Step 3 start
        UsersApi.GetUsersOptions options = usersApi.new GetUsersOptions();
        options.setAccountId(accountId);
        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        SimpleDateFormat df = new SimpleDateFormat(simpleDateFormat);
        // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 10)));
        options.setLastModifiedSince(nowAsISO);
        OrganizationUsersResponse modifiedUsers = usersApi.getUsers(organizationId, options);
        // Step 3 end

        // Step 5 start
        for (OrganizationUserResponse user : modifiedUsers.getUsers()) {
            UsersApi.GetUserProfilesOptions profilesOptions = usersApi.new GetUserProfilesOptions();
            profilesOptions.setEmail(user.getEmail());
            UsersDrilldownResponse res = usersApi.getUserProfiles(organizationId, profilesOptions);
            resultsArr.add(res.getUsers().get(0));
        }
        // Step 5 end

        return resultsArr;
    }
}

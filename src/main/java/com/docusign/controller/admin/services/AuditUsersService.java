package com.docusign.controller.admin.services;

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

    public static ArrayList<UserDrilldownResponse> auditUsers(
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId
    ) throws ApiException {
        ArrayList<UserDrilldownResponse> auditedUsers = new ArrayList<>();

        //ds-snippet-start:Admin5Step3
        UsersApi.GetUsersOptions options = usersApi.new GetUsersOptions();
        options.setAccountId(accountId);
        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        SimpleDateFormat df = new SimpleDateFormat(simpleDateFormat);
        // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        Integer tenDaysBeforeToday = 1000 * 60 * 60 * 24 * 10;
        String nowAsISO = df.format(new Date(System.currentTimeMillis() - tenDaysBeforeToday));
        options.setLastModifiedSince(nowAsISO);
        OrganizationUsersResponse modifiedUsers = usersApi.getUsers(organizationId, options);
        //ds-snippet-end:Admin5Step3

        //ds-snippet-start:Admin5Step5
        for (OrganizationUserResponse user : modifiedUsers.getUsers()) {
            //ds-snippet-end:Admin5Step5
            //ds-snippet-start:Admin5Step4
            UsersApi.GetUserProfilesOptions profilesOptions = usersApi.new GetUserProfilesOptions();
            profilesOptions.setEmail(user.getEmail());
            //ds-snippet-end:Admin5Step4
            //ds-snippet-start:Admin5Step5
            UsersDrilldownResponse res = usersApi.getUserProfiles(organizationId, profilesOptions);
            auditedUsers.add(res.getUsers().get(0));
        }
        //ds-snippet-end:Admin5Step5

        return auditedUsers;
    }
}

package com.docusign.common;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;


@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Data
public class WorkArguments {
    private String item;
    private String quantity;
    private String signerEmail;
    private String signerName;
    private String ccEmail;
    private String ccName;
    private String clickwrapName;
    private String signerEmail2;
    private String signerName2;
    private String ccEmail2;
    private String ccName2;
    private String status;
    private String startingView;
    private String dsReturnUrl;
    private String dsPingUrl;
    private String signerClientId;
    private String signerCheckedEmail;
    private String signerCheckedName;
    private String signerNotCheckedEmail;
    private String signerNotCheckedName;
    private String docSelect;
    private String accessCode;
    private String brandName;
    private String language;
    private String brandId;
    private String templateId;
    private String profileId;
    private String groupId;
    private String permissionProfileName;
    private String phoneNumber;
    private Integer roleId;
    private String roomName;
    private Integer roomTemplateId;
    private Integer roomId;
    private Integer officeId;
    private UUID formId;
    private String formGroupName;
    private UUID formGroupId;
    private String startDate;
    private String endDate;
    private String ccPhoneNumber;
    private String countryCode;
    private String ccCountryCode;
}

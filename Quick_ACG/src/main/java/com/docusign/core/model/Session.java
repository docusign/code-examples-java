package com.docusign.core.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.UUID;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Session implements Serializable {
    private static final long serialVersionUID = 2695379118371574037L;

    private String accountId;
    private String accountName;
    private String basePath;
    private String statusCFR;
    private String roomsBasePath;
    private String envelopeId;
    private String templateId;
    private String templateName;
    private String permissionProfileId;
    private String permissionProfileName;
    private String apiIndexPath;
    private boolean refreshToken = false;
    private String clickwrapId;
    private String clickwrapVersionNumber;
    private String exportId;
    private String importId;
    private UUID orgId;
    public UUID bulkListId;
}

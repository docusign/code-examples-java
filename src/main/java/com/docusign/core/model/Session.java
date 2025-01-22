package com.docusign.core.model;

import com.docusign.esign.client.auth.OAuth;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Session implements Serializable {
    private static final long serialVersionUID = 2695379118371574037L;

    public UUID bulkListId;

    public Long tokenExpirationTime;

    private String accountId;

    private String createdUserId;

    private String accountName;

    private String basePath;

    private String roomsBasePath;

    private String envelopeId;

    private String templateId;

    private String templateName;

    private String statusCFR;

    private List<EnvelopeDocumentInfo> envelopeDocuments;

    private String permissionProfileId;

    private String permissionProfileName;

    private String apiIndexPath;

    private AuthType authTypeSelected = AuthType.AGC;

    private boolean refreshToken;

    private String clickwrapId;

    private String clickwrapVersionNumber;

    private String exportId;

    private String importId;

    private String webformTemplateId;

    private String emailAddress;

    private UUID orgId;

    private OAuth.Account oauthAccount;

    private String monitorExampleRedirect;

    private String workflowId;

    private String instanceId;

    private Boolean isPKCEWorking = true;
}

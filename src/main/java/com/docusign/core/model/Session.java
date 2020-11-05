package com.docusign.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Session implements Serializable {
    private static final long serialVersionUID = 2695379118371574037L;

    private String accountId;
    private String accountName;
    private String basePath;
    private String envelopeId;
    private String templateName;
    private List<EnvelopeDocumentInfo> envelopeDocuments;
    private String permissionProfileId;
    private String permissionProfileName;
    private String apiIndexPath;
    private AuthType authTypeSelected =  AuthType.AGC;
    private boolean refreshToken = false;

}

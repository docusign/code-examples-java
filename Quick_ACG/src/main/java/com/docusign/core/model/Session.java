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

    public Long tokenExpirationTime;

    private String accountId;

    private String accountName;

    private String basePath;

    private String envelopeId;

    private String statusCFR;

    private Boolean isPKCEWorking = true;
}

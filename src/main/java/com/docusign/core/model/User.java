package com.docusign.core.model;

import lombok.Data;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 8959083682339598499L;

    private String name;
    private String accessToken;
}

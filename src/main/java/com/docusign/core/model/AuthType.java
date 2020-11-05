package com.docusign.core.model;

public enum AuthType {
    //Authorization Code Grant
    AGC("Authorization Code Grant"),
    //JWT Code Grant
    JWT("JSON Web Token Grant");

    final String value;

    AuthType(String value) {
        this.value = value;
    }
}

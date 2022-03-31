package com.docusign.core.model;

public enum ApiType {
    ESIGNATURE("eSignature API", new String[]{"signature"}),
    ROOMS("Rooms API", new String[]{"signature", "dtr.rooms.read", "dtr.rooms.write", "dtr.documents.read", "dtr.documents.write", "dtr.profile.read", "dtr.profile.write", "dtr.company.read", "dtr.company.write", "room_forms"}),
    CLICK("Click API", new String[] {"click.manage", "click.send"}),
    MONITOR("Monitor API", new String[] {"signature", "impersonation"}),
    ADMIN("Admin API",  new String[] {"user_write", "signature", "impersonation", "group_read", "organization_read", "permission_read", "user_read", "account_read", "domain_read", "identity_provider_read"});

    final String value;
    final String[] scopes;

    ApiType(String value, String[] scopes) {
        this.value = value;
        this.scopes = scopes;
    }

    public String getValue() {
        return value;
    }

    public String[] getScopes() {
        return scopes;
    }
}
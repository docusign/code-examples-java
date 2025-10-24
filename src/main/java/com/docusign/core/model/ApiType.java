package com.docusign.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ApiType {
    ESIGNATURE("eSignature API", new String[] { "signature" }, "eg"),
    ROOMS("Rooms API", new String[] { "signature", "dtr.rooms.read", "dtr.rooms.write", "dtr.documents.read",
            "dtr.documents.write", "dtr.profile.read", "dtr.profile.write", "dtr.company.read",
            "dtr.company.write", "room_forms" }, "r"),
    CONNECT("Connect", new String[] {}, "con"),
    CONNECTEDFIELDS("Connected Fields API", new String[] { "signature", "adm_store_unified_repo_read" }, "cf"),
    CLICK("Click API", new String[] { "click.manage", "click.send" }, "c"),
    MONITOR("Monitor API", new String[] { "signature", "impersonation" }, "m"),
    ADMIN("Admin API", new String[] { "user_write", "signature", "impersonation", "group_read", "organization_read",
            "permission_read", "user_read", "account_read", "domain_read", "identity_provider_read", "user_data_redact",
            "asset_group_account_read", "asset_group_account_clone_write", "asset_group_account_clone_read",
            "organization_sub_account_read", "organization_sub_account_write" }, "a"),
    WEBFORMS("WebForms API",
            new String[] { "signature", "webforms_read", "webforms_instance_read", "webforms_instance_write" }, "web"),
    NAVIGATOR("Navigator API", new String[] { "signature", "adm_store_unified_repo_read" }, "nav"),
    MAESTRO("Maestro API", new String[] { "signature", "aow_manage" }, "mae"),
    NOTARY("Notary API", new String[] { "signature" }, "n");

    final String value;

    final String[] scopes;

    final String codeName;

    ApiType(String value, String[] scopes, String codeName) {
        this.value = value;
        this.scopes = scopes;
        this.codeName = codeName;
    }

    public static ApiType giveTypeByName(String exampleName) {
        String exampleCode = exampleName.replaceAll("[^A-Za-z]", "");
        return Arrays.stream(ApiType.values())
                .filter(x -> exampleCode.equalsIgnoreCase(x.getCodeName()))
                .findFirst()
                .get();
    }

    public static List<String> getScopeList() {
        List<String> scopes = new ArrayList<>();
        for (ApiType scope : ApiType.values()) {
            scopes.addAll(Arrays.asList(scope.getScopes()));
        }
        return scopes;
    }

    public String[] getScopes() {
        return scopes;
    }

    public String getCodeName() {
        return codeName;
    }
}
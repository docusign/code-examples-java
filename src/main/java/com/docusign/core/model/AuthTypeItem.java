package com.docusign.core.model;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class AuthTypeItem {

    private String key;
    private String value;

    public AuthTypeItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static AuthTypeItem convert(AuthType authType) {
        return new AuthTypeItem(authType.name(), authType.value);
    }

    public static AuthType convert(AuthTypeItem authType) {
        return AuthType.valueOf(authType.key);
    }

    public static List<AuthTypeItem> list() {
        return EnumSet.allOf(AuthType.class).stream()
                .map(AuthTypeItem::convert)
                .collect(Collectors.toList());
    }
}

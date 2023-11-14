package com.docusign.core.model;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ApiTypeItem {

    private String key;

    private String value;

    public ApiTypeItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static ApiTypeItem convert(ApiType apiType) {
        return new ApiTypeItem(apiType.name(), apiType.value);
    }

    public static ApiType convert(ApiTypeItem apiType) {
        return ApiType.valueOf(apiType.key);
    }

    public static List<ApiTypeItem> list() {
        return EnumSet.allOf(ApiType.class).stream()
                .map(ApiTypeItem::convert)
                .collect(Collectors.toList());
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
}

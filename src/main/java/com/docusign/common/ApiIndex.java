package com.docusign.common;

public enum ApiIndex {
    ESIGNATURE("/pages/esignature/index", "/restapi"),
    ROOMS("/pages/rooms/index", "/restapi"),
    CLICK("/pages/click/index", "/clickapi"),
    MONITOR("/pages/monitor/index", "");

    private final String indexPath;
    private final String baseUrlSuffix;

    ApiIndex(final String indexPath, final String baseUrlSuffix) {
        this.indexPath = indexPath;
        this.baseUrlSuffix = baseUrlSuffix;
    }

    public String getBaseUrlSuffix() {
        return baseUrlSuffix;
    }

    @Override
    public String toString() {
        return this.indexPath;
    }
}

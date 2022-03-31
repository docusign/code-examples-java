package com.docusign.common;

public enum ApiIndex {
    ESIGNATURE("/pages/esignature/index", "/restapi", "/eg001"),
    ROOMS("/pages/rooms/index", "/restapi", "/r001"),
    CLICK("/pages/click/index", "/clickapi", "/c001"),
    MONITOR("/pages/monitor/index", "", "/m001"),
    ADMIN("/pages/admin/index", "/management", "/a001");

    private final String indexPath;
    private final String baseUrlSuffix;
    private final String firstExamplePath;

    ApiIndex(final String indexPath, final String baseUrlSuffix, final String firstExamplePath) {
        this.indexPath = indexPath;
        this.baseUrlSuffix = baseUrlSuffix;
        this.firstExamplePath = firstExamplePath;
    }

    public String getBaseUrlSuffix() {
        return baseUrlSuffix;
    }

    public String getPathOfFirstExample() {
        return firstExamplePath;
    }

    @Override
    public String toString() {
        return this.indexPath;
    }
}

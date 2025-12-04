package com.docusign.common;

public enum ApiIndex {
    ESIGNATURE("/pages/esignature/index", "/restapi", "/eg001", "/eg"),
    ROOMS("/pages/rooms/index", "/restapi", "/r001", "/r"),
    CLICK("/pages/click/index", "/clickapi", "/c001", "/c"),
    MONITOR("/pages/monitor/index", "", "/m001", "/m"),
    ADMIN("/pages/admin/index", "/management", "/a001", "/a"),
    CONNECT("/pages/connect/index", "", "/con001", "/con"),
    NAVIGATOR("/pages/navigator/index", "/restapi", "/nav001", "/nav"),
    NOTARY("/pages/notary/index", "/restapi", "/n004", "/n"),
    WEBFORMS("/pages/webforms/index", "/restapi", "/web001", "/web"),
    MAESTRO("/pages/maestro/index", "/restapi", "/mae001", "/mae"),
    CONNECTEDFIELDS("/pages/connectedfields/index", "/restapi", "/cf001", "/cf"),
    WORKSPACES("/pages/workspaces/index", "/restapi", "/work001", "/work");

    private final String indexPath;

    private final String baseUrlSuffix;

    private final String firstExamplePath;

    private final String examplesPathCode;

    ApiIndex(final String indexPath, final String baseUrlSuffix, final String firstExamplePath,
            final String examplesPathCode) {
        this.indexPath = indexPath;
        this.baseUrlSuffix = baseUrlSuffix;
        this.firstExamplePath = firstExamplePath;
        this.examplesPathCode = examplesPathCode;
    }

    public String getBaseUrlSuffix() {
        return baseUrlSuffix;
    }

    public String getPathOfFirstExample() {
        return firstExamplePath;
    }

    public String getExamplesPathCode() {
        return examplesPathCode;
    }

    @Override
    public String toString() {
        return this.indexPath;
    }
}
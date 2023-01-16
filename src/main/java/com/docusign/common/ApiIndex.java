package com.docusign.common;

public enum ApiIndex {
    ESIGNATURE( "/restapi", "/eg001"),
    ROOMS( "/restapi", "/r001"),
    CLICK("/clickapi", "/c001"),
    MONITOR( "", "/m001"),
    ADMIN("/management", "/a001");

    private final String baseUrlSuffix;
    private final String firstExamplePath;


    ApiIndex(final String baseUrlSuffix, final String firstExamplePath) {
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
}

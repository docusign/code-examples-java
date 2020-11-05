package com.docusign.common;

public enum ApiIndex {
    ESIGNATURE("/pages/esignature/index"),
    ROOMS("/pages/rooms/index");

    private final String indexPath;

    ApiIndex(final String indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    public String toString() {
        return this.indexPath;
    }
}

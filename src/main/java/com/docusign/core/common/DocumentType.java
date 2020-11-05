package com.docusign.core.common;

import javax.ws.rs.core.MediaType;

/**
 * This enumeration represents supported document types for signing.
 */
public enum DocumentType {
    HTML("html", MediaType.TEXT_HTML),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PDF("pdf", "application/pdf");

    private final String defaultFileExtention;
    private final String mime;

    DocumentType(String defaultFileExtention, String mime) {
        this.defaultFileExtention = defaultFileExtention;
        this.mime = mime;
    }

    public String getDefaultFileExtention() {
        return defaultFileExtention;
    }

    public String getMime() {
        return mime;
    }
}

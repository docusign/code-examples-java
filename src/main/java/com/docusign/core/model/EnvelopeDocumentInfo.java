package com.docusign.core.model;

import lombok.Value;

import java.io.Serializable;


@Value
public class EnvelopeDocumentInfo implements Serializable {
    private static final long serialVersionUID = 8423772017910727550L;

    private String name;
    private String type;
    private String documentId;
}

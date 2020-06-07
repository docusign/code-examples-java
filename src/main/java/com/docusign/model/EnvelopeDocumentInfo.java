package com.docusign.model;

import java.io.Serializable;

import lombok.Value;


@Value
public class EnvelopeDocumentInfo implements Serializable {
    private static final long serialVersionUID = 8423772017910727550L;

    private String name;
    private String type;
    private String documentId;
}

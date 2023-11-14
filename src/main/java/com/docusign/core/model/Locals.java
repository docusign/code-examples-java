package com.docusign.core.model;

import com.docusign.DSConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Locals {
    private DSConfiguration dsConfig;

    private Session session;

    private User user;

    private String messages;
}

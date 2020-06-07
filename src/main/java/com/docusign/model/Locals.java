package com.docusign.model;

import com.docusign.DSConfiguration;

public class Locals {
    private DSConfiguration dsConfig;
    private Session session;
    private User user;
    private String messages;

    public DSConfiguration getDsConfig() {
        return dsConfig;
    }

    public void setDsConfig(DSConfiguration dsConfig) {
        this.dsConfig = dsConfig;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessages() {
        return messages;
    }
}

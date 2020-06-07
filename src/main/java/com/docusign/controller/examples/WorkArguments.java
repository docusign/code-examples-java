package com.docusign.controller.examples;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class WorkArguments {

    private String item;
    private String quantity;
    private String signerEmail;
    private String signerName;
    private String ccEmail;
    private String ccName;
    private String accountId;
    private String envelopeId;
    private String status;
    private String templateName;
    private String templateId;
    private String redirectUrl = "pages/example_done";
    private String startingView;
    private String dsReturnUrl;
    private String dsPingUrl;
    private String signerClientId;
    private String documentId;
    private JSONObject envelopeDocuments;

    public String getSignerEmail() {
        return signerEmail;
    }

    public String getSignerName() {
        return signerName;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStartingView() {
        return startingView;
    }

    public void setStartingView(String startingView) {
        this.startingView = startingView;
    }

    public String getDsReturnUrl() {
        return dsReturnUrl;
    }

    public void setDsReturnUrl(String dsReturnUrl) {
        this.dsReturnUrl = dsReturnUrl;
    }

    public String getDsPingUrl() {
        return dsPingUrl;
    }

    public void setDsPingUrl(String dsPingUrl) {
        this.dsPingUrl = dsPingUrl;
    }

    public String getSignerClientId() {
        return signerClientId;
    }

    public void setSignerClientId(String signerClientId) {
        this.signerClientId = signerClientId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setSignerEmail(String signerEmail) {
        this.signerEmail = signerEmail;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public JSONObject getEnvelopeDocuments() {
        return envelopeDocuments;
    }

    public void setEnvelopeDocuments(JSONObject envelopeDocuments) {
        this.envelopeDocuments = envelopeDocuments;
    }
}

package com.docusign.core.model;

import com.docusign.esign.model.AccountRoleSettings;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRoleSettingsPatch extends AccountRoleSettings {

    @JsonProperty("signingUiVersion")
    private String signingUiVersion = "";

    public AccountRoleSettings signingUiVersion(String signingUiVersion) {
        this.signingUiVersion = signingUiVersion;
        return this;
    }
}

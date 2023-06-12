package com.docusign.core.model;

import com.docusign.esign.model.AccountRoleSettings;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class AccountRoleSettingsPatch extends AccountRoleSettings {

    @JsonProperty("signingUiVersion")
    private String signingUiVersion = null;

    public AccountRoleSettings signingUiVersion(String signingUiVersion) {
        this.signingUiVersion = signingUiVersion;
        return this;
    }

    /**
     *
     * @return signingUiVersion
     **/
    @Schema(example = "null", description = "")
    public String getSigningUiVersion() {
        return signingUiVersion;
    }


}

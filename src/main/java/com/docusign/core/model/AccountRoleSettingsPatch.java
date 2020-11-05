package com.docusign.core.model;

import com.docusign.esign.model.AccountRoleSettings;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(example = "null", value = "")
    public String getSigningUiVersion() {
        return signingUiVersion;
    }


}

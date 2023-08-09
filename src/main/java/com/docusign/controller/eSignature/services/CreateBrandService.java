package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Brand;
import com.docusign.esign.model.BrandsResponse;

import java.util.List;

public final class CreateBrandService {
    public static BrandsResponse createBrand(
            AccountsApi accountsApi,
            String brandName,
            String language,
            String accountId
    ) throws ApiException {
        Brand brand = new Brand()
                .brandName(brandName)
                .defaultBrandLanguage(language)
                .brandLanguages(List.of(language));

        // Step 4. Call the eSignature REST API
        //ds-snippet-start:eSign28Step4
        return accountsApi.createBrand(accountId, brand);
        //ds-snippet-end:eSign28Step4
    }
}

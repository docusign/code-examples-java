package com.services.eSignature;

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

        // Step 5: Call the eSignature REST API
        return accountsApi.createBrand(accountId, brand);
    }
}

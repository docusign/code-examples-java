package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Brand;
import com.docusign.esign.model.BrandsResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class CreateBrandService {
    public static BrandsResponse createBrand(
            AccountsApi accountsApi,
            String brandName,
            String language,
            String accountId) throws ApiException {
        Brand brand = new Brand()
                .brandName(brandName)
                .defaultBrandLanguage(language)
                .brandLanguages(List.of(language));

        // Step 4. Call the eSignature REST API
        // ds-snippet-start:eSign28Step4
        var brandResponse = accountsApi.createBrandWithHttpInfo(accountId, brand);
        Map<String, List<String>> headers = brandResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return brandResponse.getData();
        // ds-snippet-end:eSign28Step4
    }
}

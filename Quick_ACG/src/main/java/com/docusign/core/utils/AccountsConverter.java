package com.docusign.core.utils;

import com.docusign.esign.client.auth.OAuth;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AccountsConverter {

    public static Map<String, Object> convert(OAuth.Account account) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("account_id", account.getAccountId());
        res.put("is_default", account.getIsDefault());
        res.put("account_name", account.getAccountName());
        res.put("base_uri", account.getBaseUri());
        return res;
    }

    public static OAuth.Account convert(Map<String, Object> account) {
        OAuth.Account res = new OAuth.Account();
        res.setAccountId((String) account.get("account_id"));
        res.setIsDefault(String.valueOf(account.get("is_default")));
        res.setAccountName((String) account.get("account_name"));
        res.setBaseUri((String) account.get("base_uri"));
        return res;
    }
}

package com.docusign.core.security.jwt;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth.Account;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.core.utils.AccountsConverter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public class JWTUserInfoTokenService implements ResourceServerTokenServices {

    private static final String AUTHORITIES = "ROLE_USER";

    private final JWTAuthorizationCodeResourceDetails resource;
    private ApiClient apiClient;

    public JWTUserInfoTokenService(JWTAuthorizationCodeResourceDetails resource) {
        this.resource = resource;
        apiClient = resource.getApiClient();
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        UserInfo userInfo;
        try {
            userInfo = apiClient.getUserInfo(apiClient.getAccessToken());
        } catch (ApiException e) {
            throw new AuthenticationServiceException("Cannot authenticate", e);
        }
        return convert(userInfo, resource.getClientId());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private static OAuth2Authentication convert(UserInfo userInfo, String clientId) {
        List<Account> accounts = userInfo.getAccounts();
        Object principal = userInfo.getName() == null ? "unknown" : userInfo.getName();
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(AUTHORITIES);
        OAuth2Request request = new OAuth2Request(null, clientId, null, true, null,
                null, null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        Map<String, Object> details = new LinkedHashMap<>();
        List<Map<String, Object>> accountsAsMap = accounts.stream()
                .map(AccountsConverter::convert)
                .collect(Collectors.toList());
        details.put("accounts", accountsAsMap);
        token.setDetails(details);
        return new OAuth2Authentication(request, token);
    }
}

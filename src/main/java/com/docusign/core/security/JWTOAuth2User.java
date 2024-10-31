package com.docusign.core.security;

import com.docusign.esign.client.auth.OAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class JWTOAuth2User implements OAuth2User {
    private List <GrantedAuthority> authorities;

    private Map <String, Object> attributes;

    private String sub;

    private String name;

    private String givenName;

    private String familyName;

    private OAuth.OAuthToken accessToken;

    private String email;

    private List <Map <String, Object>> accounts;

    private String created;

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(List < String > scopes) {
        String authoritiesString = "ROLE_USER";
        for (String scope: scopes) {
            authoritiesString += ",SCOPE_" + scope;
        }
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);
    }

    @Override
    public Map <String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap <> ();
            this.attributes.put("sub", this.getSub());
            this.attributes.put("name", this.getName());
            this.attributes.put("given_name", this.getGivenName());
            this.attributes.put("family_name", this.getFamilyName());
            this.attributes.put("created", this.getCreated());
            this.attributes.put("email", this.getEmail());
            this.attributes.put("accounts", this.getAccounts());
            this.attributes.put("access_token", this.getAccessToken());
        }
        return attributes;
    }

    public String getSub() {
        return this.sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public OAuth.OAuthToken getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(OAuth.OAuthToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List <Map <String, Object>> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List <OAuth.Account> accounts) {
        this.accounts = new ArrayList <> ();
        for (OAuth.Account account: accounts) {
            ObjectMapper mapObject = new ObjectMapper();
            Map <String, Object> mapObj = mapObject.convertValue(account, Map.class);
            this.accounts.add(mapObj);
        }
    }
}
package com.example.assignment_4.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenController {

    @GetMapping("/tokens")
    public Map<String, Object> getTokens(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient) {
        
        Map<String, Object> tokens = new HashMap<>();
        
        if (oidcUser == null) {
            tokens.put("error", "User not authenticated");
            return tokens;
        }
        
        // User Information
        tokens.put("username", oidcUser.getPreferredUsername());
        tokens.put("email", oidcUser.getEmail() != null ? oidcUser.getEmail() : "");
        tokens.put("name", oidcUser.getFullName() != null ? oidcUser.getFullName() : "");
        
        // ID Token (OIDC)
        if (oidcUser.getIdToken() != null) {
            tokens.put("idToken", oidcUser.getIdToken().getTokenValue());
            tokens.put("idTokenClaims", oidcUser.getClaims());
        } else {
            tokens.put("idToken", null);
            tokens.put("idTokenClaims", null);
        }
        
        // Access Token
        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            tokens.put("accessToken", authorizedClient.getAccessToken().getTokenValue());
            tokens.put("accessTokenScopes", authorizedClient.getAccessToken().getScopes());
            tokens.put("accessTokenExpiresAt", authorizedClient.getAccessToken().getExpiresAt());
        } else {
            tokens.put("accessToken", null);
            tokens.put("accessTokenScopes", null);
            tokens.put("accessTokenExpiresAt", null);
        }
        
        // Refresh Token
        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {
            var refreshToken = authorizedClient.getRefreshToken();
            if (refreshToken != null) {
                tokens.put("refreshToken", refreshToken.getTokenValue());
                tokens.put("refreshTokenIssuedAt", refreshToken.getIssuedAt());
            } else {
                tokens.put("refreshToken", null);
                tokens.put("refreshTokenIssuedAt", null);
            }
        } else {
            tokens.put("refreshToken", null);
            tokens.put("message", "Refresh token không có sẵn. Đảm bảo scope 'offline_access' được cấu hình.");
        }
        
        return tokens;
    }
    
    @GetMapping("/userinfo")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> userInfo = new HashMap<>();
        
        if (oidcUser == null) {
            userInfo.put("error", "User not authenticated");
            return userInfo;
        }
        
        userInfo.put("sub", oidcUser.getSubject());
        userInfo.put("preferredUsername", oidcUser.getPreferredUsername());
        userInfo.put("email", oidcUser.getEmail() != null ? oidcUser.getEmail() : "");
        userInfo.put("emailVerified", Boolean.TRUE.equals(oidcUser.getEmailVerified()));
        userInfo.put("name", oidcUser.getFullName() != null ? oidcUser.getFullName() : "");
        userInfo.put("givenName", oidcUser.getGivenName() != null ? oidcUser.getGivenName() : "");
        userInfo.put("familyName", oidcUser.getFamilyName() != null ? oidcUser.getFamilyName() : "");
        userInfo.put("allClaims", oidcUser.getClaims());
        
        return userInfo;
    }
}

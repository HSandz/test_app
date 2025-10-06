package com.example.assignment_4.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/user")
    public String user(Model model, 
                      @AuthenticationPrincipal OidcUser oidcUser,
                      @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient) {
        
        if (oidcUser == null) {
            return "redirect:/";
        }
        
        model.addAttribute("username", oidcUser.getPreferredUsername());
        model.addAttribute("email", oidcUser.getEmail() != null ? oidcUser.getEmail() : "");
        model.addAttribute("name", oidcUser.getFullName() != null ? oidcUser.getFullName() : "");
        model.addAttribute("claims", oidcUser.getClaims());
        
        // ID Token
        if (oidcUser.getIdToken() != null) {
            model.addAttribute("idToken", oidcUser.getIdToken().getTokenValue());
        } else {
            model.addAttribute("idToken", "N/A");
        }
        
        // Access Token
        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());
        } else {
            model.addAttribute("accessToken", "N/A");
        }
        
        // Refresh Token (nếu có)
        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {
            model.addAttribute("refreshToken", authorizedClient.getRefreshToken().getTokenValue());
        } else {
            model.addAttribute("refreshToken", "N/A");
        }
        
        return "user";
    }
}

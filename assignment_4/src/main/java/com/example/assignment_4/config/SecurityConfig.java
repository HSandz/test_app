package com.example.assignment_4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/public/**", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(this.oidcUserService())
                )
                .defaultSuccessUrl("/user", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );
        
        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            // Load user info from the userinfo endpoint if available
            OidcUser oidcUser;
            try {
                oidcUser = delegate.loadUser(userRequest);
            } catch (IllegalArgumentException e) {
                // If delegate fails due to missing preferred_username, 
                // create OidcUser directly with 'sub' as name attribute
                return new DefaultOidcUser(
                    java.util.Collections.singleton(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")
                    ),
                    userRequest.getIdToken(),
                    "sub" // Use 'sub' claim as the name attribute
                );
            }

            // If delegate succeeds, recreate with 'sub' as name attribute
            return new DefaultOidcUser(
                oidcUser.getAuthorities(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "sub"
            );
        };
    }
}

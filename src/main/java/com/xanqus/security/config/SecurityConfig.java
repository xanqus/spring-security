package com.xanqus.security.config;

import com.xanqus.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests(authroize -> authroize.antMatchers("/user/**")
                        .authenticated()
                        .antMatchers("/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll())
                    .formLogin()
                    .loginPage("/loginForm")
                    //.usernameParameter("username2")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                .and()
                    .oauth2Login()
                    .loginPage("/loginForm")
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService)
                .and()
                .and()
                .build();
    }

}

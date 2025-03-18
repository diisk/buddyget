package br.dev.diisk.infra.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.dev.diisk.web.middlewares.security.CustomAccessDeniedHandler;
import br.dev.diisk.web.middlewares.security.CustomAuthenticationEntryPoint;
import br.dev.diisk.web.middlewares.security.SecurityFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .exceptionHandling(exc -> {
                    exc.authenticationEntryPoint(customAuthenticationEntryPoint);
                    exc.accessDeniedHandler(customAccessDeniedHandler);
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // .authorizeHttpRequests(
                // auth -> auth
                // // .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**")
                // // .permitAll()
                // // .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // // .requestMatchers(HttpMethod.POST, "/auth/register")
                // // .hasAuthority(UserPermission.ADD_USER.getAuthority())
                // .anyRequest().denyAll())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // static GrantedAuthorityDefaults rolePrefix() {
    // return new GrantedAuthorityDefaults("");
    // }

}

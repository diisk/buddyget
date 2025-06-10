package br.dev.diisk.infra.shared.configurations.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class SecurityAlgorithmConfiguration {

    @Value("${api.security.token-secret}")
    private String secret;

    @Bean
    Algorithm algorithmHCM256() {
        return Algorithm.HMAC256(secret);
    }

}

package br.dev.diisk.infra.shared.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.dev.diisk.application.shared.services.ITokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    @Value("${spring.application.name}")
    private String appName;

    private final Algorithm algorithm;

    @Override
    public String generateToken(String subject) {
        String token = JWT.create()
                .withIssuer(appName)
                .withSubject(subject)
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
        return token;
    }

    private final Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(-3));
    }

    @Override
    public String getSubject(String token) {
        return JWT.require(algorithm)
                .withIssuer(appName)
                .build().verify(token)
                .getSubject();

    }

}

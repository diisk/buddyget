package br.dev.diisk.application.cases.auth;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.services.ITokenService;
import br.dev.diisk.domain.entities.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthRenewCase{

    private final ITokenService tokenService;

    public String execute(User user) {
        String token = tokenService.generateToken(user);
        return token;
    }

}

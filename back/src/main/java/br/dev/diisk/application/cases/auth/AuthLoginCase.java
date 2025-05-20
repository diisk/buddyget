package br.dev.diisk.application.cases.auth;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.services.IAuthService;
import br.dev.diisk.application.services.ITokenService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.authentication.InvalidUserException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthLoginCase{

    private final IAuthService authService;
    private final ITokenService tokenService;

    public String execute(String email, String password) {
        User user;
        try {
            user = authService.authenticate(email, password);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new InvalidUserException(getClass());
        }
        String token = tokenService.generateToken(user);
        return token;
    }

}

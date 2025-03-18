package br.dev.diisk.application.cases.auth;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.auth.LoginRequest;
import br.dev.diisk.application.exceptions.authentication.InvalidUserException;
import br.dev.diisk.application.interfaces.auth.IAuthService;
import br.dev.diisk.application.interfaces.auth.ITokenService;
import br.dev.diisk.domain.entities.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthLoginCase{

    private final IAuthService authService;
    private final ITokenService tokenService;

    public String execute(LoginRequest dto) {
        User user;
        try {
            user = authService.authenticate(dto.getEmail(), dto.getPassword());
        } catch (Exception ex) {
            System.err.println(ex);
            throw new InvalidUserException(getClass());
        }
        String token = tokenService.generateToken(user);
        return token;
    }

}

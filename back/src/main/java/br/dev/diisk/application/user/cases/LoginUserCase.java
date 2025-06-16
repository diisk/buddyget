package br.dev.diisk.application.user.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.shared.services.IAuthService;
import br.dev.diisk.application.shared.services.ITokenService;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserCase {

    private final IAuthService authService;
    private final ITokenService tokenService;

    @Transactional
    public String execute(String email, String password) {
        if (email == null || email.isBlank())
            throw new NullOrEmptyException(getClass(), "email");

        if (password == null || password.isBlank())
            throw new NullOrEmptyException(getClass(), "password");

        User user = authService.authenticate(email, password);
        String token = tokenService.generateToken(user.getId().toString());
        return token;
    }
}

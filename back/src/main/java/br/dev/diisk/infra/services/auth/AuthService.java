package br.dev.diisk.infra.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.services.IAuthService;
import br.dev.diisk.domain.entities.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public User authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        return (User) auth.getPrincipal();
    }

}

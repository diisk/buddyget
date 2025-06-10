package br.dev.diisk.infra.services.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.shared.services.ISecurityService;
import br.dev.diisk.domain.value_objects.Password;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService implements ISecurityService {

    @Override
    public String encryptPassword(Password password) {
        return encryptPassword(password.getValue());
    }

    @Override
    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public Boolean matchPasswords(String password, String encryptedPassword) {
        return new BCryptPasswordEncoder().matches(password, encryptedPassword);

    }

}

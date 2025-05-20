package br.dev.diisk.application.cases.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthRegisterCase {

    private final IUserRepository userRepository;
    private final IUserPerfilRepository userPerfilRepository;

    @Transactional
    public User execute(String name, String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null)
            throw new ValueAlreadyInDatabaseException(getClass(), "email");

        UserPerfil defaultUserPerfil = userPerfilRepository.findByName("DEFAULT");
        if (defaultUserPerfil == null)
            throw new DbValueNotFoundException(getClass(), "name");

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        User newUser = new User(name, email, encryptedPassword, defaultUserPerfil);
        userRepository.save(newUser);
        return newUser;
    }

}

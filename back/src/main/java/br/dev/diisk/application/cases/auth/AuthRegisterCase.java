package br.dev.diisk.application.cases.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.auth.RegisterRequest;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.user.UserPerfil;
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
    public User execute(RegisterRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);
        if (user != null)
            throw new ValueAlreadyInDatabaseException(getClass(), "email");

        UserPerfil defaultUserPerfil = userPerfilRepository.findByName("Default");
        if (defaultUserPerfil == null)
            throw new DbValueNotFoundException(getClass(), "name");

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        User newUser = new User(dto.getName(), dto.getEmail(), encryptedPassword, defaultUserPerfil);
        userRepository.save(newUser);
        return newUser;
    }

}

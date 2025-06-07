package br.dev.diisk.application.cases.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.enums.user.UserPerfilEnum;
import br.dev.diisk.domain.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.domain.value_objects.Email;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddUserCase {

    private final IUserRepository userRepository;
    private final IUserPerfilRepository userPerfilRepository;

    @Transactional
    public User execute(String name, String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null)
            throw new DatabaseValueConflictException(getClass(), email);

        String perfilName = UserPerfilEnum.DEFAULT.name();
        UserPerfil defaultUserPerfil = userPerfilRepository.findByName(perfilName);
        if (defaultUserPerfil == null)
            throw new DatabaseValueNotFoundException(getClass(), perfilName);

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        User newUser = new User(name, new Email(email), encryptedPassword, defaultUserPerfil);
        userRepository.save(newUser);
        return newUser;
    }
}

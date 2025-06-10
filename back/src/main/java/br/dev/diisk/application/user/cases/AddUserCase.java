package br.dev.diisk.application.user.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.shared.services.ISecurityService;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.shared.value_objects.Email;
import br.dev.diisk.domain.shared.value_objects.Password;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserPerfil;
import br.dev.diisk.domain.user.enums.UserPerfilEnum;
import br.dev.diisk.domain.user.interfaces.IUserPerfilRepository;
import br.dev.diisk.domain.user.interfaces.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddUserCase {

    private final IUserRepository userRepository;
    private final IUserPerfilRepository userPerfilRepository;
    private final ISecurityService securityService;

    @Transactional
    public User execute(String name, String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null)
            throw new DatabaseValueConflictException(getClass(), email);

        String perfilName = UserPerfilEnum.DEFAULT.name();
        UserPerfil defaultUserPerfil = userPerfilRepository.findByName(perfilName);
        if (defaultUserPerfil == null)
            throw new DatabaseValueNotFoundException(getClass(), perfilName);

        String encryptedPassword = securityService.encryptPassword(new Password(password));

        User newUser = new User(name, new Email(email), encryptedPassword, defaultUserPerfil);
        userRepository.save(newUser);
        return newUser;
    }
}

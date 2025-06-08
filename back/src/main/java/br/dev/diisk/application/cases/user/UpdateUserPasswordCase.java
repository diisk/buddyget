package br.dev.diisk.application.cases.user;

import org.springframework.stereotype.Service;
import br.dev.diisk.application.dtos.user.UpdateUserPasswordParams;
import br.dev.diisk.application.services.ISecurityService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.exceptions.UnauthorizedException;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.domain.value_objects.Password;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserPasswordCase {

    private final IUserRepository userRepository;
    private final ISecurityService securityService;

    @Transactional
    public void execute(User user, UpdateUserPasswordParams params) {
        String password = params.getPassword();
        if (password == null || password.isBlank())
            throw new NullOrEmptyException(getClass(), "password");

        if (params.getNewPassword() == null || params.getNewPassword().isBlank())
            throw new NullOrEmptyException(getClass(), "newPassword");

        if (!securityService.matchPasswords(password, user.getEncryptedPassword()))
            throw new UnauthorizedException(getClass(), "Senha atual inválida.");

        Password newPassword = new Password(params.getNewPassword());

        user.update(null, securityService.encryptPassword(newPassword));
        userRepository.save(user);

    }
}

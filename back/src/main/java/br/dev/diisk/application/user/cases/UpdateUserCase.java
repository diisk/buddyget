package br.dev.diisk.application.user.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.user.dtos.UpdateUserParams;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.interfaces.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserCase {

    private final IUserRepository userRepository;

    @Transactional
    public User execute(User user, UpdateUserParams params) {
        Boolean save = user.update(params.getName(), null);
        if (save)
            userRepository.save(user);

        return user;
    }
}

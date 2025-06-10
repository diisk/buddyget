package br.dev.diisk.domain.user.interfaces;

import java.util.Optional;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;
import br.dev.diisk.domain.user.User;

public interface IUserRepository extends IBaseRepository<User> {

    Optional<User> findByEmail(String email);
}

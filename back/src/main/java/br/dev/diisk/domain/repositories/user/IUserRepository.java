package br.dev.diisk.domain.repositories.user;

import java.util.Optional;

import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface IUserRepository extends IBaseRepository<User,Long> {

    Optional<User> findByEmail(String email);
}

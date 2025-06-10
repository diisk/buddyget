package br.dev.diisk.infra.user.repositories;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.infra.shared.BaseRepository;
import br.dev.diisk.infra.user.jpas.UserJPA;

@Repository
public class UserRepository extends BaseRepository<UserJPA, User> implements IUserRepository {

    public UserRepository(UserJPA jpa) {
        super(jpa);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail_Value(email);
    }

}

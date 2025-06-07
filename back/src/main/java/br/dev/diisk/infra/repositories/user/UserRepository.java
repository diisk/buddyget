package br.dev.diisk.infra.repositories.user;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.infra.jpas.user.UserJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

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

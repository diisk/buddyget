package br.dev.diisk.infra.repositories.user;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import br.dev.diisk.infra.jpas.user.UserPerfilJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class UserPerfilRepository extends BaseRepository<UserPerfilJPA, UserPerfil>
        implements IUserPerfilRepository {

    public UserPerfilRepository(UserPerfilJPA jpa) {
        super(jpa);
    }

    @Override
    public UserPerfil findByName(String name) {
        return jpa.findByName(name);
    }

}

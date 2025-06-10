package br.dev.diisk.infra.user.repositories;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.user.UserPerfil;
import br.dev.diisk.domain.user.interfaces.IUserPerfilRepository;
import br.dev.diisk.infra.shared.BaseRepository;
import br.dev.diisk.infra.user.jpas.UserPerfilJPA;

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

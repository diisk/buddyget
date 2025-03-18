package br.dev.diisk.domain.repositories.user;

import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.repositories.IBaseRepository;



public interface IUserPerfilRepository extends IBaseRepository<UserPerfil,Long> {

    UserPerfil findByName(String name);
}

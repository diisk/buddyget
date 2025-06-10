package br.dev.diisk.domain.user.interfaces;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;
import br.dev.diisk.domain.user.UserPerfil;



public interface IUserPerfilRepository extends IBaseRepository<UserPerfil> {

    UserPerfil findByName(String name);
}

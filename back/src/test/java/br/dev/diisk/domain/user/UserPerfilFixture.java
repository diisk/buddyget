package br.dev.diisk.domain.user;

import java.util.Collections;

public class UserPerfilFixture {
    public static UserPerfil umPerfilPadrao(Long id) {
        UserPerfil perfil = new UserPerfil();
        perfil.setId(id);
        perfil.setName("Perfil Teste");
        perfil.setLevel(1);
        perfil.setPermissions(Collections.emptyList());
        return perfil;
    }
}

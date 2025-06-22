package br.dev.diisk.domain.user;

import br.dev.diisk.domain.shared.value_objects.Email;

public class UserFixture {
    public static User umUsuarioComId(Long id) {
        UserPerfil perfil = br.dev.diisk.domain.user.UserPerfilFixture.umPerfilPadrao(1L);
        User user = new User(
            "User Teste",
            new Email("user@email.com"),
            "senhaForte123",
            perfil
        );
        user.setId(id);
        return user;
    }
}

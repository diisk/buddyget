package br.dev.diisk.infra.user.jpas;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.user.UserPerfil;



public interface UserPerfilJPA extends JpaRepository<UserPerfil, Long> {

    UserPerfil findByName(String name);
}

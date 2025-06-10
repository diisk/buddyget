package br.dev.diisk.infra.user.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.user.User;


public interface UserJPA extends JpaRepository<User, Long> {
    Optional<User> findByEmail_Value(String email);
}

package br.dev.diisk.presentation.user.dtos;

import br.dev.diisk.domain.entities.user.User;

public record UserResponse(
        Long id,
        String email,
        String nome) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName());
    }

}

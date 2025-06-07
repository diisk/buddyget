package br.dev.diisk.presentation.dtos.auth;

import br.dev.diisk.domain.entities.user.User;

public record RegisterResponse(Long id, String email) {
        public RegisterResponse(User user) {
                this(user.getId(), user.getEmail());
        }
}

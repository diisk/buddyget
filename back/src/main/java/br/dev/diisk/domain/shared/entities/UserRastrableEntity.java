package br.dev.diisk.domain.shared.entities;

import br.dev.diisk.domain.user.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class UserRastrableEntity extends RastreableEntity {
    @ManyToOne(optional = false)
    protected User user;

    public UserRastrableEntity(User user) {
        this.user = user;
        validate();
    }

    public Long getUserId() {
        return user.getId();
    }

    private void validate() {
        if (user == null)
            throw new RuntimeException("Unexpected error: user is null");

    }
}

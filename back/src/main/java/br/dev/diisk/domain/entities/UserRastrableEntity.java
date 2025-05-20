package br.dev.diisk.domain.entities;

import br.dev.diisk.domain.entities.user.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class UserRastrableEntity extends RastreableEntity {
    @ManyToOne(optional = false)
    protected final User user;

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

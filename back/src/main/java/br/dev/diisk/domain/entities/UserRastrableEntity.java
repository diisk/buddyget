package br.dev.diisk.domain.entities;

import br.dev.diisk.domain.entities.user.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class UserRastrableEntity extends RastreableEntity {
    @ManyToOne(optional = false)
    private User user;
}

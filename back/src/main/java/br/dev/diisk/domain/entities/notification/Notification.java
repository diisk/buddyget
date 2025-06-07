package br.dev.diisk.domain.entities.notification;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.value_objects.Metadata;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "notifications")
@Entity
public class Notification extends UserRastrableEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean readed = false;

    @Embedded
    private Metadata metadata;

    public Notification(User user, String title, String description, Metadata metadata) {
        super(user);
        this.title = title;
        this.description = description;
        this.metadata = metadata;
        validate();
    }

    private void validate() {
        if (title == null || title.isBlank()) {
            throw new NullOrEmptyException(getClass(), "title");
        }

        if (description == null || description.isBlank()) {
            throw new NullOrEmptyException(getClass(), "description");
        }
    }
}
package br.dev.diisk.domain.notification;

import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.Metadata;
import br.dev.diisk.domain.user.User;
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
    @Getter(value = lombok.AccessLevel.NONE)
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

    public Boolean isReaded(){
        return readed;
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
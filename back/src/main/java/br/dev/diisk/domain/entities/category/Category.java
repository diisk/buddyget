package br.dev.diisk.domain.entities.category;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.value_objects.HexadecimalColor;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "categories")
public class Category extends UserRastrableEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String iconName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "color", nullable = false))
    private HexadecimalColor color;

    @Enumerated(EnumType.STRING)
    private CategoryTypeEnum type;

    public Category(User user, String description, CategoryTypeEnum type) {
        super(user);
        this.description = description;
        this.type = type;
        validate();
    }

    private void validate() {
        if (description == null || description.isBlank()) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Description cannot be null or empty.",
                    description);
        }

        if (name == null || name.isBlank()) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Name cannot be null or empty.", name);
        }

        if (iconName == null || iconName.isBlank()) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Icon name cannot be null or empty.", iconName);

        }

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }

    }
}

package br.dev.diisk.domain.category;

import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "categories")
@NoArgsConstructor
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

    public Category(User user, String name, String description, String iconName, CategoryTypeEnum type,
            HexadecimalColor color) {
        super(user);
        this.name = name;
        this.description = description;
        this.iconName = iconName;
        this.color = color;
        this.type = type;
        validate();
    }

    public String getColorString() {
        return color != null ? color.getValue() : null;
    }

    public Boolean update(String name, String description, String iconName,
            HexadecimalColor color) {
        Boolean updated = false;
        if (name != null && !this.name.equals(name)) {
            this.name = name;
            updated = true;
        }
        if (description != null && !this.description.equals(description)) {
            this.description = description;
            updated = true;
        }
        if (iconName != null && !this.iconName.equals(iconName)) {
            this.iconName = iconName;
            updated = true;
        }
        if (color != null && !this.color.equals(color)) {
            this.color = color;
            updated = true;
        }
        validate();
        return updated;
    }

    public Long getUserId() {
        return getUser().getId();
    }

    private void validate() {
        if (description == null || description.isBlank())
            throw new NullOrEmptyException(getClass(), "description");

        if (name == null || name.isBlank())
            throw new NullOrEmptyException(getClass(), "name");

        if (iconName == null || iconName.isBlank())
            throw new NullOrEmptyException(getClass(), "iconName");

        if (type == null)
            throw new NullOrEmptyException(getClass(), "type");

        if (color == null)
            throw new NullOrEmptyException(getClass(), "color");
    }
}

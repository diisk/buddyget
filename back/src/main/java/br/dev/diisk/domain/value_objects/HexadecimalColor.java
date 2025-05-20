package br.dev.diisk.domain.value_objects;

import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class HexadecimalColor {

    @Column(name = "color", nullable = true)
    private final String value;

    public HexadecimalColor(String value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (value == null || !value.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Invalid hexadecimal color value.", value);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        HexadecimalColor that = (HexadecimalColor) obj;
        return value.equals(that.value);
    }

    @Override
    public String toString() {
        return value;
    }
}

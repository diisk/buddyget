package br.dev.diisk.domain.shared.value_objects;

import java.util.Map;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class HexadecimalColor {

    @Column(name = "color", nullable = true)
    private String value;

    public HexadecimalColor(String value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (value == null || !value.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new BusinessException(getClass(), "Hexadecimal inválido.", Map.of("color", value));
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

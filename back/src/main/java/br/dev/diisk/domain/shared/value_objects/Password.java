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
public class Password {

    @Column(name = "password", nullable = false, unique = false)
    private String value;

    public Password(String value) {
        this.value = value;
        validate();
    }

    private void validate() {
        String minLengthRegex = ".{8,}";
        String upperCaseRegex = ".*[A-Z].*";
        String lowerCaseRegex = ".*[a-z].*";
        String digitRegex = ".*\\d.*";
        String specialCharRegex = ".*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?-].*";

        if (!value.matches(minLengthRegex)
                || !value.matches(upperCaseRegex)
                || !value.matches(lowerCaseRegex)
                || !value.matches(digitRegex)
                || !value.matches(specialCharRegex)) {
            throw new BusinessException(getClass(),
                    "Senha inválida. Deve ter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial.",
                    Map.of("senha", value));
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Password password = (Password) obj;
        return value.equals(password.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
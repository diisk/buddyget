package br.dev.diisk.domain.value_objects;

import java.util.Map;

import br.dev.diisk.domain.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Password {

    @Column(name = "password", nullable = false, unique = false)
    private final String value;

    public Password(String value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (!value.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")) {
            throw new BusinessException(getClass(),
                    "Senha inválida. Deve ter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula e um dígito.",
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
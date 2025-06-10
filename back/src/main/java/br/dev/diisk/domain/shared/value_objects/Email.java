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
public class Email {

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    public Email(String value) {
        this.value = value;
        validate();
    }

    private void validate() {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (value == null || !value.matches(emailRegex))
            throw new BusinessException(getClass(), "Email inválido.", Map.of("email", value));

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Email email = (Email) obj;
        return value.equals(email.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
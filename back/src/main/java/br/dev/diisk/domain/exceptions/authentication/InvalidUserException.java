package br.dev.diisk.domain.exceptions.authentication;

import br.dev.diisk.domain.exceptions.CustomRuntimeException;
import lombok.Getter;

@Getter
public class InvalidUserException extends CustomRuntimeException {

    public InvalidUserException(Class<?> classObject) {
        super(classObject, "{exception.invalid.user}");
    }

}

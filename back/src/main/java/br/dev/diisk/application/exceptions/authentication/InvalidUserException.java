package br.dev.diisk.application.exceptions.authentication;

import br.dev.diisk.application.exceptions.CustomRuntimeException;
import br.dev.diisk.domain.MessageUtils;
import lombok.Getter;

@Getter
public class InvalidUserException extends CustomRuntimeException {

    public InvalidUserException(Class<?> classObject) {
        super(classObject, MessageUtils.getMessage("exception.invalid.user"));
    }

}

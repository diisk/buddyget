package br.dev.diisk.domain.exceptions;

import java.util.Map;

import br.dev.diisk.domain.enums.ExceptionTypeEnum;

public class NullOrEmptyException extends DomainException {

    public NullOrEmptyException(String message, Class<?> classObject) {
        super(classObject, ExceptionTypeEnum.NULL_VALUE, message, null);
    }

    public NullOrEmptyException(Class<?> classObject, String fieldName) {
        super(classObject, ExceptionTypeEnum.NULL_VALUE, "O campo não pode ser nulo ou vazio.",
                Map.of("campo", fieldName));
    }

}

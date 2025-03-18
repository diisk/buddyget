package br.dev.diisk.application.exceptions;

import lombok.Getter;

@Getter
public class BadRequestFieldCustomRuntimeException extends FieldCustomRuntimeException {

    public BadRequestFieldCustomRuntimeException(Class<?> classObject, String message, String field) {
        super(classObject, message, field);
    }

}

package br.dev.diisk.domain.exceptions;

import lombok.Getter;


@Getter
public class BadRequestValueCustomRuntimeException extends ValueCustomRuntimeException{

    public BadRequestValueCustomRuntimeException(Class<?> classObject, String message, String value) {
        super(classObject, message, value);
    }
    
}

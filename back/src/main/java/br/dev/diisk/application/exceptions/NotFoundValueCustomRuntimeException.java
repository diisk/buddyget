package br.dev.diisk.application.exceptions;

import lombok.Getter;


@Getter
public class NotFoundValueCustomRuntimeException extends ValueCustomRuntimeException{

    public NotFoundValueCustomRuntimeException(Class<?> classObject, String message, String value) {
        super(classObject, message, value);
    }
    
}

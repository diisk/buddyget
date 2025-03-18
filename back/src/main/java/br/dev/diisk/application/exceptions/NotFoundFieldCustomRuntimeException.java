package br.dev.diisk.application.exceptions;

import lombok.Getter;


@Getter
public class NotFoundFieldCustomRuntimeException extends FieldCustomRuntimeException{

    public NotFoundFieldCustomRuntimeException(Class<?> classObject, String message, String field) {
        super(classObject, message, field);
    }
    
}

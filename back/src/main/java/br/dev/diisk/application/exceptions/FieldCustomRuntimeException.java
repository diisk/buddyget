package br.dev.diisk.application.exceptions;

import lombok.Getter;


@Getter
public class FieldCustomRuntimeException extends CustomRuntimeException{

    private String field;

    public FieldCustomRuntimeException(Class<?> classObject, String message, String field) {
        super(classObject, message);
        this.field = field;
    }
    
}

package br.dev.diisk.domain.exceptions;

import lombok.Getter;

@Getter
public class ValueCustomRuntimeException extends CustomRuntimeException {

    private String value;

    public ValueCustomRuntimeException(Class<?> classObject, String message, String value) {
        super(classObject, message);
        this.value = value;
    }

}

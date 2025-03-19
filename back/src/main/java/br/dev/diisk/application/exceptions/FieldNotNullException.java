package br.dev.diisk.application.exceptions;

public class FieldNotNullException extends BadRequestFieldCustomRuntimeException {

    public FieldNotNullException(Class<?> classObject, String field) {
        super(classObject, "{exception.not-null.field}", field);
    }

}

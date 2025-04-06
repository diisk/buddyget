package br.dev.diisk.application.exceptions;

public class RequiredFieldException extends BadRequestFieldCustomRuntimeException {

    public RequiredFieldException(Class<?> classObject, String field) {
        super(classObject, "{exception.required.field}", field);
    }

}

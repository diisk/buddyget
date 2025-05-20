package br.dev.diisk.domain.exceptions;

public class RequiredFieldException extends BadRequestFieldCustomRuntimeException {

    public RequiredFieldException(Class<?> classObject, String field) {
        super(classObject, "{exception.required.field}", field);
    }

}

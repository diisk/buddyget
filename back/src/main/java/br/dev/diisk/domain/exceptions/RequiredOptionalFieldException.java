package br.dev.diisk.domain.exceptions;

public class RequiredOptionalFieldException extends BadRequestFieldCustomRuntimeException {

    public RequiredOptionalFieldException(Class<?> classObject, String field) {
        super(classObject, "{exception.required.optional-field}", field);
    }

}

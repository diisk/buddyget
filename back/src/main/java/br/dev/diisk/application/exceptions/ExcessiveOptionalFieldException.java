package br.dev.diisk.application.exceptions;

public class ExcessiveOptionalFieldException extends BadRequestFieldCustomRuntimeException {

    public ExcessiveOptionalFieldException(Class<?> classObject, String field) {
        super(classObject, "{exception.invalid.optional-field}", field);
    }

}

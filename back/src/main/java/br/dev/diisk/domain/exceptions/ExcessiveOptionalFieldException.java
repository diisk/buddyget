package br.dev.diisk.domain.exceptions;

public class ExcessiveOptionalFieldException extends BadRequestFieldCustomRuntimeException {

    public ExcessiveOptionalFieldException(Class<?> classObject, String field) {
        super(classObject, "{exception.invalid.optional-field}", field);
    }

}

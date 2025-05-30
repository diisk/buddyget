package br.dev.diisk.domain.exceptions;

public class EnumValueNotFoundException extends NotFoundValueCustomRuntimeException {

    public EnumValueNotFoundException(Class<?> classObject, String value) {
        super(classObject, "{exception.not-found.enum}", value);
    }

}

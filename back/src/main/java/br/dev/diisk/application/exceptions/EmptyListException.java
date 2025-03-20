package br.dev.diisk.application.exceptions;

import lombok.Getter;

@Getter
public class EmptyListException extends NotFoundValueCustomRuntimeException {

    public EmptyListException(Class<?> classObject, String value) {
        super(classObject, "{exception.empty.list}", value);
    }

}

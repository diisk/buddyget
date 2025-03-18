package br.dev.diisk.application.exceptions;

import br.dev.diisk.domain.MessageUtils;

public class FieldNotNullException extends BadRequestFieldCustomRuntimeException {

    public FieldNotNullException(Class<?> classObject, String field) {
        super(classObject, MessageUtils.getMessage("exception.not-null.field"), field);
    }

}

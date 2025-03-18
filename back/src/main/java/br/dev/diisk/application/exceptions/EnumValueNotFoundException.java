package br.dev.diisk.application.exceptions;

import br.dev.diisk.domain.MessageUtils;

public class EnumValueNotFoundException extends NotFoundValueCustomRuntimeException {

    public EnumValueNotFoundException(Class<?> classObject, String value) {
        super(classObject, MessageUtils.getMessage("exception.not-found.enum"), value);
    }

}

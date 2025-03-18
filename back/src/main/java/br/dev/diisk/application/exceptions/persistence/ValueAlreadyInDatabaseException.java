package br.dev.diisk.application.exceptions.persistence;

import br.dev.diisk.application.exceptions.BadRequestFieldCustomRuntimeException;
import br.dev.diisk.domain.MessageUtils;
import lombok.Getter;

@Getter
public class ValueAlreadyInDatabaseException extends BadRequestFieldCustomRuntimeException {

    public ValueAlreadyInDatabaseException(Class<?> classObject, String field) {
        super(classObject, MessageUtils.getMessage("exception.exists.db-value"), field);
    }

}

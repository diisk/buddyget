package br.dev.diisk.application.exceptions.persistence;

import br.dev.diisk.application.exceptions.BadRequestFieldCustomRuntimeException;
import br.dev.diisk.domain.MessageUtils;
import lombok.Getter;

@Getter
public class DbValueNotFoundException extends BadRequestFieldCustomRuntimeException {

    public DbValueNotFoundException(Class<?> classObject, String field) {
        super(classObject, MessageUtils.getMessage("exception.not-found.db-value"), field);
    }

}

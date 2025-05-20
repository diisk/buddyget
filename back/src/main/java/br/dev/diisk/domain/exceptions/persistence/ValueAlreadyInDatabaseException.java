package br.dev.diisk.domain.exceptions.persistence;

import br.dev.diisk.domain.exceptions.BadRequestFieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class ValueAlreadyInDatabaseException extends BadRequestFieldCustomRuntimeException {

    public ValueAlreadyInDatabaseException(Class<?> classObject, String field) {
        super(classObject, "{exception.exists.db-value}", field);
    }

}

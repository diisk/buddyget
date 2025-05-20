package br.dev.diisk.domain.exceptions.persistence;

import br.dev.diisk.domain.exceptions.BadRequestFieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class DbValueNotFoundException extends BadRequestFieldCustomRuntimeException {

    public DbValueNotFoundException(Class<?> classObject, String field) {
        super(classObject, "{exception.not-found.db-value}", field);
    }

}

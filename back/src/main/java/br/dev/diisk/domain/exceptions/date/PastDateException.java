package br.dev.diisk.domain.exceptions.date;

import br.dev.diisk.domain.exceptions.BadRequestFieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class PastDateException extends BadRequestFieldCustomRuntimeException {

    public PastDateException(Class<?> classObject, String field) {
        super(classObject, "{exception.past-date}", field);
    }

}

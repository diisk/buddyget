package br.dev.diisk.application.exceptions.date;

import br.dev.diisk.application.exceptions.BadRequestFieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class PastDateException extends BadRequestFieldCustomRuntimeException {

    public PastDateException(Class<?> classObject, String field) {
        super(classObject, "{exception.past-date}", field);
    }

}

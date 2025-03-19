package br.dev.diisk.application.exceptions.date;

import br.dev.diisk.application.exceptions.BadRequestFieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class FutureDateException extends BadRequestFieldCustomRuntimeException {

    public FutureDateException(Class<?> classObject, String field) {
        super(classObject, "{exception.future-date}", field);
    }

}

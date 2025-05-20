package br.dev.diisk.domain.exceptions.date;

import br.dev.diisk.domain.exceptions.BadRequestFieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class FutureDateException extends BadRequestFieldCustomRuntimeException {

    public FutureDateException(Class<?> classObject, String field) {
        super(classObject, "{exception.future-date}", field);
    }

}

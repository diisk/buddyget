package br.dev.diisk.application.exceptions.date;

import br.dev.diisk.application.exceptions.BadRequestFieldCustomRuntimeException;
import br.dev.diisk.domain.MessageUtils;
import lombok.Getter;

@Getter
public class FutureDateException extends BadRequestFieldCustomRuntimeException {

    public FutureDateException(Class<?> classObject, String field) {
        super(classObject, MessageUtils.getMessage("exception.future-date"), field);
    }

}

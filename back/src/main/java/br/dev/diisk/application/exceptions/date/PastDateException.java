package br.dev.diisk.application.exceptions.date;

import br.dev.diisk.application.exceptions.BadRequestFieldCustomRuntimeException;
import br.dev.diisk.domain.MessageUtils;
import lombok.Getter;

@Getter
public class PastDateException extends BadRequestFieldCustomRuntimeException {

    public PastDateException(Class<?> classObject, String field) {
        super(classObject, MessageUtils.getMessage("exception.past-date"), field);
    }

}

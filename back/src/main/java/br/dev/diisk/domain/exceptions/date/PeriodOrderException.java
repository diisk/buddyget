package br.dev.diisk.domain.exceptions.date;

import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import lombok.Getter;

@Getter
public class PeriodOrderException extends BadRequestValueCustomRuntimeException {

    public PeriodOrderException(Class<?> classObject, String value) {
        super(classObject, "{exception.invalid.period-order}", value);
    }

}

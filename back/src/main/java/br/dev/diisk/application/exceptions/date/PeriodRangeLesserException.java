package br.dev.diisk.application.exceptions.date;

import br.dev.diisk.application.exceptions.BadRequestValueCustomRuntimeException;
import lombok.Getter;

@Getter
public class PeriodRangeLesserException extends BadRequestValueCustomRuntimeException {

    public PeriodRangeLesserException(Class<?> classObject, String value) {
        super(classObject, "{exception.invalid.period-range-lesser}", value);
    }

}

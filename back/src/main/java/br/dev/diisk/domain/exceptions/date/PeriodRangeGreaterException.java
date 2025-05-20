package br.dev.diisk.domain.exceptions.date;

import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import lombok.Getter;

@Getter
public class PeriodRangeGreaterException extends BadRequestValueCustomRuntimeException {

    public PeriodRangeGreaterException(Class<?> classObject, String value) {
        super(classObject, "{exception.invalid.period-range-greater}", value);
    }

}

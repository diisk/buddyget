package br.dev.diisk.domain.shared.validations;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.shared.value_objects.Period;

import java.time.LocalDateTime;
import java.util.Map;

public class DateOutOfRangeValidation implements IValidationStrategy {

    private final LocalDateTime referenceDate;
    private final Period period;

    public DateOutOfRangeValidation(LocalDateTime referenceDate, Period period) {
        this.referenceDate = referenceDate;
        this.period = period;
    }

    public void validate(Class<?> classObj) {
        if (referenceDate.isBefore(period.getStartDate())
                || (period.getEndDate() != null && referenceDate.isAfter(period.getEndDate())))
            throw new BusinessException(getClass(),
                    "A data de referência deve estar dentro do período.",
                    Map.of("referenceDate", referenceDate.toString(), "period", period.toString()));
    }

}

package br.dev.diisk.domain.shared.validations;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

import java.time.LocalDateTime;
import java.util.Map;

public class StartDateHigherOrEqualEndDateValidation implements IValidationStrategy {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public StartDateHigherOrEqualEndDateValidation(LocalDateTime startDate, LocalDateTime endDate) {
        this.endDate = endDate;
        this.startDate = startDate;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate))
            throw new BusinessException(classObj, "A data não pode ser maior ou igual a data atual.",
                    Map.of("startDate", startDate.toString(), "endDate", endDate.toString()));
    }

}

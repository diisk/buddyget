package br.dev.diisk.domain.shared.validations;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

import java.time.LocalDateTime;
import java.util.Map;

public class DateLesserOrEqualNowValidation implements IValidationStrategy {

    private final LocalDateTime date;
    private final String fieldName;

    public DateLesserOrEqualNowValidation(LocalDateTime date, String fieldName) {
        this.date = date;
        this.fieldName = fieldName;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (date.isAfter(LocalDateTime.now()) || date.isEqual(LocalDateTime.now()))
            throw new BusinessException(classObj, "A data não pode ser maior ou igual a data atual.",
                    Map.of(fieldName, date.toString()));
    }
}

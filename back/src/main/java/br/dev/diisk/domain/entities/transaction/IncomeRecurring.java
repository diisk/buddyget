package br.dev.diisk.domain.entities.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.validations.category.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.value_objects.DataRange;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "incomes_recurrings")
public class IncomeRecurring extends Recurring {

    public IncomeRecurring(String description, DataRange period, Category category, BigDecimal value,
            LocalDateTime date, User user) {
        super(description, category, value, period, user);
        validate();
    }

    private void validate() {
        new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.INCOME)
                .validate(getClass());
    }

}

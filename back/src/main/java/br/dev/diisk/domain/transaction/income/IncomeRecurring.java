package br.dev.diisk.domain.transaction.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.transaction.Recurring;
import br.dev.diisk.domain.user.User;
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

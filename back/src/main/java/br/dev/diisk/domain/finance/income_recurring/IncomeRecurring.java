package br.dev.diisk.domain.finance.income_recurring;

import java.math.BigDecimal;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.finance.Recurring;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "incomes_recurrings")
@NoArgsConstructor
public class IncomeRecurring extends Recurring {

    public IncomeRecurring(String description, DataRange period, Category category,
            BigDecimal value, User user) {
        super(description, category, value, period, user);
        validate();
    }

    private void validate() {
        new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.INCOME)
                .validate(getClass());
    }

}

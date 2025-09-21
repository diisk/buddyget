package br.dev.diisk.domain.finance.income_recurring;

import java.math.BigDecimal;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.finance.Recurring;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "incomes_recurrings")
@NoArgsConstructor
public class IncomeRecurring extends Recurring {

    @OneToOne(optional = true)
    private Goal goal;

    public IncomeRecurring(String description, Period period, Category category,
            BigDecimal value, User user) {
        super(description, category, value, period, user);
        validate();
    }

    public void addGoal(Goal goal) {
        if (this.goal != null)
            throw new BusinessException(getClass(), "Esta recorrência já possui uma meta associada.");

        if (goal == null)
            throw new BusinessException(getClass(), "A meta informada não pode ser nula.");

        if (goal.getUserId() != getUserId()) {
            throw new BusinessException(getClass(), "A meta informada não pertence ao usuário");
        }
        this.goal = goal;
    }

    private void validate() {
        new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.INCOME)
                .validate(getClass());
    }

}

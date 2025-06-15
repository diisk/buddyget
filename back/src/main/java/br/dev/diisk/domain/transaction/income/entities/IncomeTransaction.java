package br.dev.diisk.domain.transaction.income.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.transaction.Transaction;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "incomes")
public class IncomeTransaction extends Transaction {

    @ManyToOne(optional = true)
    private IncomeRecurring incomeRecurring;

    @ManyToOne(optional = true)
    private Goal goal;

    public IncomeTransaction(String description, Category category, BigDecimal value, LocalDateTime date,
            User user) {
        super(description, category, value, date, user);
        validate();
    }

    public void addGoal(Goal goal) {
        if (this.goal != null)
            throw new BusinessException(getClass(), "A meta já foi definida");

        if (goal == null)
            throw new NullOrEmptyException(getClass(), "goal");

        if (goal.getUserId() != getUserId()) {
            throw new BusinessException(getClass(), "A meta deve pertencer ao mesmo usuário",
                    Map.of("goalId", goal.getId().toString()));
        }

        this.goal = goal;
    }

    private void validate() {
        new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.INCOME)
                .validate(getClass());
    }

}

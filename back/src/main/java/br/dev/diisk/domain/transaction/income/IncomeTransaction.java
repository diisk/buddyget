package br.dev.diisk.domain.transaction.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.transaction.Transaction;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "incomes")
public class IncomeTransaction extends Transaction {

    @ManyToOne(optional = true)
    private IncomeRecurring incomeRecurring;

    @ManyToOne(optional = true)
    private CreditCard creditCard;

    @ManyToOne(optional = true)
    private Goal goal;

    public IncomeTransaction(String description, Category category, BigDecimal value, LocalDateTime date,
            User user) {
        super(description, category, value, date, user);
        validate();
    }

    private void validate() {
        new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.INCOME)
                .validate(getClass());
    }

}

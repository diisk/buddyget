package br.dev.diisk.domain.transaction.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.category.validations.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardIfExistsNotBelongUserValidation;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.transaction.Transaction;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.domain.wish_list.WishListItemIfExistsNotBelongUserValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "expenses")
public class ExpenseTransaction extends Transaction {

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @ManyToOne(optional = true)
    private ExpenseRecurring expenseRecurring;

    @ManyToOne(optional = true)
    private CreditCard creditCard;

    @OneToOne(optional = true)
    private WishListItem wishItem;

    @ManyToOne(optional = true)
    private Goal goal;

    public ExpenseTransaction(
            String description,
            Category category,
            BigDecimal value,
            LocalDateTime date,
            User user) {
        super(description, category, value, date, user);
        validate();
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.EXPENSE),
                new CategoryNotBelongUserValidation(category, getUserId()),
                new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId()),
                new WishListItemIfExistsNotBelongUserValidation(wishItem, getUserId()));

        if (expenseRecurring != null && expenseRecurring.getUserId() != getUserId()) {
            throw new BusinessException(getClass(),
                    "A despesa recorrente não pertence ao usuário",
                    Map.of("expenseRecurringId", expenseRecurring.getId().toString()));
        }

        if (goal != null && goal.getUserId() != getUserId()) {
            throw new BusinessException(getClass(),
                    "A meta não pertence ao usuário",
                    Map.of("goalId", goal.getId().toString()));
        }

        validations.forEach(validation -> validation.validate(getClass()));
    }

}

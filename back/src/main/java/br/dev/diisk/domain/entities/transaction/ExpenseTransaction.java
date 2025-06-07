package br.dev.diisk.domain.entities.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.goal.Goal;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.wish_list.WishListItem;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.BusinessException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import br.dev.diisk.domain.validations.category.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.validations.category.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.validations.credit_card.CreditCardIfExistsNotBelongUserValidation;
import br.dev.diisk.domain.validations.wish_list.WishListItemIfExistsNotBelongUserValidation;
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

package br.dev.diisk.domain.finance.expense_transaction;

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
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.TransactionStatusEnum;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.domain.wish_list.WishListItemIfExistsNotBelongUserValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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

    public ExpenseTransaction(
            String description,
            Category category,
            BigDecimal value,
            LocalDateTime date,
            User user) {
        super(description, category, value, date, user);
        validate();
    }

    public LocalDateTime getPaymentDate() {
        return this.date;
    }

    public void update(String description, BigDecimal value, LocalDateTime paymentDate, LocalDateTime dueDate) {
        super.update(description, value, paymentDate);
        this.dueDate = dueDate;
    }

    public String getStatus() {
        if (getPaymentDate() != null)
            return TransactionStatusEnum.PAID.getDescription();

        if (dueDate != null && dueDate.isBefore(LocalDateTime.now()))
            return TransactionStatusEnum.LATE.getDescription();

        return TransactionStatusEnum.PENDING.getDescription();
    }

    public void addWishItem(WishListItem wishItem) {
        if (this.wishItem != null)
            throw new BusinessException(getClass(), "O item da lista de desejos já foi definido");

        if (wishItem == null)
            throw new NullOrEmptyException(getClass(), "wishItem");

        new WishListItemIfExistsNotBelongUserValidation(wishItem, getUserId())
                .validate(getClass());

        this.wishItem = wishItem;
    }

    public void addCreditCard(CreditCard creditCard) {
        if (this.creditCard != null)
            throw new BusinessException(getClass(), "O cartão de crédito já foi definido");

        if (creditCard == null)
            throw new NullOrEmptyException(getClass(), "creditCard");

        new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId())
                .validate(getClass());

        this.creditCard = creditCard;
    }

    public void addExpenseRecurring(ExpenseRecurring expenseRecurring, LocalDateTime recurringReferenceDate) {
        if (this.expenseRecurring != null)
            throw new BusinessException(getClass(), "A despesa recorrente já foi definida");

        if (expenseRecurring == null)
            throw new NullOrEmptyException(getClass(), "expenseRecurring");

        super.addRecurringDate(recurringReferenceDate);

        validateExpenseRecurring(expenseRecurring);

        this.expenseRecurring = expenseRecurring;
    }

    public void addDueDate(LocalDateTime dueDate) {
        if (this.dueDate != null)
            throw new BusinessException(getClass(), "A data de vencimento já foi definida");

        if (dueDate == null)
            throw new NullOrEmptyException(getClass(), "dueDate");

        this.dueDate = dueDate;
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.EXPENSE),
                new CategoryNotBelongUserValidation(category, getUserId()),
                new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId()),
                new WishListItemIfExistsNotBelongUserValidation(wishItem, getUserId()));

        validateExpenseRecurring(this.expenseRecurring);

        validations.forEach(validation -> validation.validate(getClass()));
    }

    private void validateExpenseRecurring(ExpenseRecurring expenseRecurring) {
        if (expenseRecurring != null && expenseRecurring.getUserId() != getUserId()) {
            throw new BusinessException(getClass(),
                    "A despesa recorrente não pertence ao usuário",
                    Map.of("expenseRecurringId", expenseRecurring.getId().toString()));
        }
    }

}

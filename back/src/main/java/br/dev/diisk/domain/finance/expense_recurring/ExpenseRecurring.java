package br.dev.diisk.domain.finance.expense_recurring;

import java.math.BigDecimal;
import java.util.List;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardIfExistsNotBelongUserValidation;
import br.dev.diisk.domain.finance.Recurring;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.domain.wish_list.WishListItemIfExistsNotBelongUserValidation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "expenses_recurrings")
@NoArgsConstructor
public class ExpenseRecurring extends Recurring {

    @ManyToOne(optional = true)
    private CreditCard creditCard;

    @OneToOne(optional = true)
    private WishListItem wishItem;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "due_day"))
    })
    @Setter
    private DayOfMonth dueDay;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "payment_day"))
    })
    protected DayOfMonth paymentDay;

    public ExpenseRecurring(String description, DataRange period, Category category,
            BigDecimal value, User user) {
        super(description, category, value, period, user);
        validate();
    }

    public void addCreditCard(CreditCard creditCard) {
        if (this.creditCard != null)
            throw new BusinessException(getClass(), "A despesa recorrente já possui um cartão de crédito definido");

        if (creditCard == null)
            throw new NullPointerException("creditCard não pode ser nulo");

        new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId()).validate(getClass());

        this.creditCard = creditCard;
    }

    public void addWishItem(WishListItem wishItem) {
        if (this.wishItem != null)
            throw new BusinessException(getClass(),
                    "A despesa recorrente já possui um item da lista de desejos definido");

        if (wishItem == null)
            throw new NullPointerException("wishItem não pode ser nulo");

        new WishListItemIfExistsNotBelongUserValidation(wishItem, getUserId()).validate(getClass());

        this.wishItem = wishItem;
    }

    public Integer getPaymentDayValue() {
        return paymentDay != null ? paymentDay.getValue() : null;
    }

    public Integer getDueDayValue() {
        return dueDay != null ? dueDay.getValue() : null;
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.EXPENSE),
                new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId()),
                new WishListItemIfExistsNotBelongUserValidation(wishItem, getUserId()));

        validations.forEach(validation -> validation.validate(getClass()));
    }

}

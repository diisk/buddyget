package br.dev.diisk.domain.transaction.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardIfExistsNotBelongUserValidation;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.transaction.Recurring;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.domain.wish_list.WishListItemIfExistsNotBelongUserValidation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "expenses_recurrings")
public class ExpenseRecurring extends Recurring {

    @ManyToOne(optional = true)
    private CreditCard creditCard;

    @OneToOne(optional = true)
    private WishListItem wishItem;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private InstallmentPlan installmentPlan;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "due_day"))
    })
    private DayOfMonth dueDay;

    public ExpenseRecurring(String description, DataRange period, Category category, BigDecimal value,
            LocalDateTime date, User user) {
        super(description, category, value, period, user);
        validate();
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

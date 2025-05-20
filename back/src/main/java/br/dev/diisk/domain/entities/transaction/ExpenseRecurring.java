package br.dev.diisk.domain.entities.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.wish_list.WishListItem;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import br.dev.diisk.domain.validations.category.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.validations.credit_card.CreditCardIfExistsNotBelongUserValidation;
import br.dev.diisk.domain.validations.wish_list.WishListItemIfExistsNotBelongUserValidation;
import br.dev.diisk.domain.value_objects.DataRange;
import br.dev.diisk.domain.value_objects.DayOfMonth;
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

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.EXPENSE),
                new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId()),
                new WishListItemIfExistsNotBelongUserValidation(wishItem, getUserId()));
        
        validations.forEach(validation -> validation.validate(getClass()));
    }

}

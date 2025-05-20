package br.dev.diisk.domain.entities.wish_list;

import java.math.BigDecimal;
import java.util.List;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.transaction.ExpenseRecurring;
import br.dev.diisk.domain.entities.transaction.ExpenseTransaction;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.enums.wish_list.WishItemPriorityEnum;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import br.dev.diisk.domain.validations.category.CategoryIdentifierNotNullValidation;
import br.dev.diisk.domain.validations.category.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.validations.category.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.validations.category.CategoryNotNullValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "wish_list_items")
public class WishListItem extends UserRastrableEntity {

    @Column(nullable = false)
    private String storeOrBrand;

    @Column(nullable = true)
    private String observation;

    @Column(nullable = true)
    private String link;

    @Column(nullable = false)
    private BigDecimal estimatedValue;

    @OneToOne(optional = true, mappedBy = "wishItem")
    @JoinColumn(name = "expense_id")
    private ExpenseTransaction expenseTransaction;

    @OneToOne(optional = true, mappedBy = "wishItem")
    @JoinColumn(name = "expense_recurring_id")
    private ExpenseRecurring expenseRecurring;

    @ManyToOne(optional = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    private WishItemPriorityEnum priority;

    public WishListItem(User user, String storeOrBrand, BigDecimal estimatedValue, Category category) {
        super(user);
        this.storeOrBrand = storeOrBrand;
        this.estimatedValue = estimatedValue;
        this.category = category;
        validate();
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryNotNullValidation(category),
                new CategoryIdentifierNotNullValidation(category),
                new CategoryNotBelongUserValidation(category, getUserId()),
                new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.EXPENSE));

        if (storeOrBrand == null || storeOrBrand.isBlank()) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Store/Brand cannot be null or empty",
                    storeOrBrand);
        }

        if (estimatedValue == null || estimatedValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Estimated value must be greater than zero",
                    estimatedValue.toString());
        }

        validations.forEach(validation -> validation.validate(getClass()));
    }

}

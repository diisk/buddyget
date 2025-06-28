package br.dev.diisk.domain.wish_list;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIdentifierNotNullValidation;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.category.validations.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.category.validations.CategoryNotNullValidation;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.user.User;
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
            throw new NullOrEmptyException(getClass(), "storeOrBrand");
        }

        if (estimatedValue == null || estimatedValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(getClass(), "O valor estimado deve ser maior que zero",
                    Map.of("estimatedValue", estimatedValue != null ? estimatedValue.toString() : "null"));
        }

        validations.forEach(validation -> validation.validate(getClass()));
    }

}

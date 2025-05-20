package br.dev.diisk.domain.entities.monthly_summary;

import java.math.BigDecimal;
import java.util.List;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import br.dev.diisk.domain.validations.category.CategoryIdentifierNotNullValidation;
import br.dev.diisk.domain.validations.category.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.validations.category.CategoryNotNullValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "monthly_summary")
public class MonthlySummary extends UserRastrableEntity {

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Long budgetLimit;

    @ManyToOne(optional = false)
    private Category category;

    public MonthlySummary(User user, Integer month, Integer year, BigDecimal amount, Long budgetLimit,
            Category category) {
        super(user);
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.budgetLimit = budgetLimit;
        this.category = category;
        validate();
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryNotNullValidation(category),
                new CategoryIdentifierNotNullValidation(category),
                new CategoryNotBelongUserValidation(category, getUserId()));

        validateMonth();
        validateYear();
        validateAmount();
        validateBudgetLimit();

        validations.forEach(validation -> validation.validate(getClass()));
    }

    private void validateBudgetLimit() {
        if (budgetLimit == null || budgetLimit <= 0) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Budget limit must be greater than zero",
                    budgetLimit.toString());
        }
    }

    private void validateAmount() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Amount must be greater than zero",
                    amount.toString());
        }
    }

    private void validateYear() {
        if (year == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Year cannot be null",
                    year.toString());
        }
    }

    private void validateMonth() {
        if (month == null || month < 1 || month > 12) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Month must be between 1 and 12",
                    month.toString());
        }
    }

}

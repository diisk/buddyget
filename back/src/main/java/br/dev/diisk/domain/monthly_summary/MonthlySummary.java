package br.dev.diisk.domain.monthly_summary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.validations.CategoryIdentifierNotNullValidation;
import br.dev.diisk.domain.category.validations.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.category.validations.CategoryNotNullValidation;
import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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

    public void addAmount(BigDecimal value) {
        validateAmount(value);
        amount = amount.add(value);

    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryNotNullValidation(category),
                new CategoryIdentifierNotNullValidation(category),
                new CategoryNotBelongUserValidation(category, getUserId()));

        validateMonth();
        validateYear();
        validateAmount(this.amount);
        validateBudgetLimit();

        validations.forEach(validation -> validation.validate(getClass()));
    }

    private void validateBudgetLimit() {
        if (budgetLimit == null || budgetLimit < 0)
            throw new BusinessException(getClass(), "O limite do orçamento deve ser maior ou igual a zero.",
                    Map.of("budgetLimit", budgetLimit != null ? budgetLimit.toString() : "null"));

    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new BusinessException(getClass(), "O valor deve ser maior ou igual a zero",
                    Map.of("amount", amount != null ? amount.toString() : "null"));

    }

    private void validateYear() {
        if (year == null)
            throw new NullOrEmptyException(getClass(), "year");

    }

    private void validateMonth() {
        if (month == null || month < 1 || month > 12)
            throw new BusinessException(getClass(), "O mês deve estar entre 1 e 12",
                    Map.of("month", month != null ? month.toString() : "null"));

    }

}

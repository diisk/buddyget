package br.dev.diisk.domain.transaction;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class GenericTransaction extends UserRastrableEntity {

    @Column(nullable = false)
    protected String description;

    @ManyToOne(optional = false)
    protected Category category;

    @Column(nullable = false)
    protected BigDecimal value;

    public GenericTransaction(String description, Category category, BigDecimal value, User user) {
        super(user);
        this.description = description;
        this.category = category;
        this.value = value;
        validate();
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryNotNullValidation(category),
                new CategoryIdentifierNotNullValidation(category),
                new CategoryNotBelongUserValidation(category, getUserId()));

        validateDescription();

        validateValue();

        validations.forEach(validation -> validation.validate(getClass()));
    }

    private void validateValue() {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    getClass(), "O valor deve ser maior que zero.",
                    Map.of("value", value != null ? value.toString() : "null"));
        }
    }

    private void validateDescription() {
        if (description == null || description.isBlank()) {
            throw new NullOrEmptyException(getClass(), "description");
        }
    }

}

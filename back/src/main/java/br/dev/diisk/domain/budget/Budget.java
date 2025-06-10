package br.dev.diisk.domain.budget;

import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIdentifierNotNullValidation;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.category.validations.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.category.validations.CategoryNotNullValidation;
import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "budget")
public class Budget extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String observation;

    @Column(nullable = false)
    private Long limitValue;

    @OneToOne(optional = false)
    private Category category;

    public Budget(User user, String description, Long limitValue, Category category) {
        super(user);
        this.description = description;
        this.limitValue = limitValue;
        this.category = category;
        validate();
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CategoryNotNullValidation(category),
                new CategoryIdentifierNotNullValidation(category),
                new CategoryNotBelongUserValidation(category, getUserId()),
                new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.EXPENSE));

        if (description == null || description.isBlank()) {
            throw new NullOrEmptyException(getClass(), "description");
        }

        if (limitValue == null || limitValue <= 0) {
            throw new BusinessException(getClass(), "O valor limite deve ser maior que zero.", 
                    Map.of("limitValue", limitValue != null ? limitValue.toString() : "null"));
        }

        validations.forEach(validation -> validation.validate(getClass()));
    }

}

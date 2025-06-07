package br.dev.diisk.domain.entities.budget;

import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.BusinessException;
import br.dev.diisk.domain.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import br.dev.diisk.domain.validations.category.CategoryIdentifierNotNullValidation;
import br.dev.diisk.domain.validations.category.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.validations.category.CategoryNotBelongUserValidation;
import br.dev.diisk.domain.validations.category.CategoryNotNullValidation;
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

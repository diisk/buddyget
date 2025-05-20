package br.dev.diisk.domain.validations.category;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;

public class CategoryNotBelongUserValidation implements IValidationStrategy {

    private final Category category;
    private final Long userId;

    public CategoryNotBelongUserValidation(Category category, Long userId) {
        this.category = category;
        this.userId = userId;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (category.getUserId() != userId)
            throw new BadRequestValueCustomRuntimeException(classObj, "Category must belong to the same user", null);

    }

}

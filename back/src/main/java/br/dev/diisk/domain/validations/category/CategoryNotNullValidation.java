package br.dev.diisk.domain.validations.category;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;

public class CategoryNotNullValidation implements IValidationStrategy {

    private final Category category;

    public CategoryNotNullValidation(Category category) {
        this.category = category;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (category == null)
            throw new BadRequestValueCustomRuntimeException(classObj, "Category cannot be null", null);

    }

}

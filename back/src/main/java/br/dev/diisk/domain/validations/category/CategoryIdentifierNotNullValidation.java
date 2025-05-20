package br.dev.diisk.domain.validations.category;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;

public class CategoryIdentifierNotNullValidation implements IValidationStrategy {

    private final Category category;

    public CategoryIdentifierNotNullValidation(Category category) {
        this.category = category;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (category.getId() == null || category.getId() <= 0)
            throw new BadRequestValueCustomRuntimeException(classObj, "Category ID is null or invalid.",
                    category.getId().toString());

    }

}

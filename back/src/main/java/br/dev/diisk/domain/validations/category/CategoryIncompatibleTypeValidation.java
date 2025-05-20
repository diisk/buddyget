package br.dev.diisk.domain.validations.category;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;

public class CategoryIncompatibleTypeValidation implements IValidationStrategy {

    private final Category category;
    private final CategoryTypeEnum type;

    public CategoryIncompatibleTypeValidation(Category category, CategoryTypeEnum type) {
        this.type = type;
        this.category = category;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (category.getType() != type)
            throw new BadRequestValueCustomRuntimeException(classObj, "Category must be of type " + type, type.name());

    }

}

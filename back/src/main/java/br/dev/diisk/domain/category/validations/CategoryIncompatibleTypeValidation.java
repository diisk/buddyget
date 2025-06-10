package br.dev.diisk.domain.category.validations;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

import java.util.Map;

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
            throw new BusinessException(classObj, "A categoria deve ser do tipo " + type,
                    Map.of("expectedType", type.name(), "actualType", category.getType().name()));
    }
}

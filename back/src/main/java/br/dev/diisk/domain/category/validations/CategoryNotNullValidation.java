package br.dev.diisk.domain.category.validations;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

public class CategoryNotNullValidation implements IValidationStrategy {

    private final Category category;

    public CategoryNotNullValidation(Category category) {
        this.category = category;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (category == null)
            throw new NullOrEmptyException(classObj, "category");
    }
}

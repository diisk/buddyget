package br.dev.diisk.domain.category.validations;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

import java.util.Map;

public class CategoryIdentifierNotNullValidation implements IValidationStrategy {

    private final Category category;

    public CategoryIdentifierNotNullValidation(Category category) {
        this.category = category;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (category.getId() == null || category.getId() <= 0)
            throw new BusinessException(classObj, "O ID da categoria é nulo ou inválido",
                    Map.of("categoryId", category.getId() != null ? category.getId().toString() : "null"));
    }
}

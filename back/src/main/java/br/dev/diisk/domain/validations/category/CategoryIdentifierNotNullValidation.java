package br.dev.diisk.domain.validations.category;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.exceptions.BusinessException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
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

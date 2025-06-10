package br.dev.diisk.domain.category.validations;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

import java.util.Map;

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
            throw new BusinessException(classObj, "A categoria deve pertencer ao mesmo usuário", 
                    Map.of("categoryId", category.getId().toString()));
    }
}

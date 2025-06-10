package br.dev.diisk.presentation.category.dtos;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;

public record CategoryResponse(
        Long id,
        String description,
        CategoryTypeEnum type,
        String color,
        String iconName) {
    public CategoryResponse(Category category) {
        this(
                category.getId(),
                category.getDescription(),
                category.getType(),
                category.getColor().getValue(),
                category.getIconName());
    }
}

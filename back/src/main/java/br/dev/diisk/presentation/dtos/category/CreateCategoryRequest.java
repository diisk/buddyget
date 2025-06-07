package br.dev.diisk.presentation.dtos.category;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;

public record CreateCategoryRequest(
    String description,
    String name,
    CategoryTypeEnum type,
    String color,
    String iconName
) {}

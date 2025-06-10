package br.dev.diisk.presentation.category.dtos;

import br.dev.diisk.domain.category.CategoryTypeEnum;

public record CreateCategoryRequest(
    String description,
    String name,
    CategoryTypeEnum type,
    String color,
    String iconName
) {}

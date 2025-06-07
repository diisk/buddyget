package br.dev.diisk.presentation.dtos.category;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;

public record CategoryResponse(
    Long id,
    String description,
    CategoryTypeEnum type,
    String color,
    String iconName
) {}

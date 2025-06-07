package br.dev.diisk.presentation.dtos.budget;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;

public record BudgetResponse(
    Long id,
    CategoryResponse category,
    Long limitValue,
    String description,
    String observation
) {}

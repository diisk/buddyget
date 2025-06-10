package br.dev.diisk.presentation.budget.dtos;

import br.dev.diisk.presentation.category.dtos.CategoryResponse;

public record BudgetResponse(
    Long id,
    CategoryResponse category,
    Long limitValue,
    String description,
    String observation
) {}

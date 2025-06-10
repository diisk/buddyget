package br.dev.diisk.presentation.budget.dtos;

import java.math.BigDecimal;

import br.dev.diisk.presentation.category.dtos.CategoryResponse;

public record DetailedBudgetResponse(
    Long id,
    CategoryResponse category,
    Long limitValue,
    BigDecimal spentValue,
    String description,
    String observation
) {}

package br.dev.diisk.presentation.dtos.budget;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;

public record DetailedBudgetResponse(
    Long id,
    CategoryResponse category,
    Long limitValue,
    BigDecimal spentValue,
    String description,
    String observation
) {}

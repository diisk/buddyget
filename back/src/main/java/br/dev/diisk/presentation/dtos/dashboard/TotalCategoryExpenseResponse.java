package br.dev.diisk.presentation.dtos.dashboard;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;

public record TotalCategoryExpenseResponse(
    CategoryResponse category,
    BigDecimal totalExpense
) {}

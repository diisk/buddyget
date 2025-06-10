package br.dev.diisk.presentation.dashboard.dtos;

import java.math.BigDecimal;

import br.dev.diisk.presentation.category.dtos.CategoryResponse;

public record TotalCategoryExpenseResponse(
    CategoryResponse category,
    BigDecimal totalExpense
) {}

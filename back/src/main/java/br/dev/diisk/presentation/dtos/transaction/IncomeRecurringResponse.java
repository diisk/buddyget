package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;

public record IncomeRecurringResponse(
    Long id,
    BigDecimal value,
    String description,
    CategoryResponse category,
    String startDate,
    String endDate,
    Integer recurringDay
) {}

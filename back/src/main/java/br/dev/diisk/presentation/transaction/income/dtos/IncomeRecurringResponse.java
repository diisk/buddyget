package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

import br.dev.diisk.presentation.category.dtos.CategoryResponse;

public record IncomeRecurringResponse(
    Long id,
    BigDecimal value,
    String description,
    CategoryResponse category,
    String startDate,
    String endDate,
    Integer recurringDay
) {}

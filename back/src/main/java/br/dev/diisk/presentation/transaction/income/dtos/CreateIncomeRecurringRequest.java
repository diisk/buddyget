package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

public record CreateIncomeRecurringRequest(
    String description,
    String startDate,
    BigDecimal value,
    String endDate,
    String recurringDay,
    Long categoryId
) {}

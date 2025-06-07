package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

public record CreateIncomeRecurringRequest(
    String description,
    String startDate,
    BigDecimal value,
    String endDate,
    String recurringDay,
    Long categoryId
) {}

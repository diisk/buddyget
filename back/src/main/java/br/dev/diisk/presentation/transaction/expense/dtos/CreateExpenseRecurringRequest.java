package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;

public record CreateExpenseRecurringRequest(
    String description,
    String startDate,
    BigDecimal value,
    BigDecimal totalValue,
    Integer installmentsCount,
    String endDate,
    String dueDay,
    String recurringDay,
    Long categoryId,
    Long creditCardId
) {}

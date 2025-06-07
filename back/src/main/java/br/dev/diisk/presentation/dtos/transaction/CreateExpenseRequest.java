package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

public record CreateExpenseRequest(
    String description,
    BigDecimal value,
    String paymentDate,
    String dueDate,
    Long categoryId,
    Long creditCardId
) {}

package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateExpenseRequest(
    String description,
    BigDecimal value,
    LocalDateTime paymentDate,
    LocalDateTime dueDate,
    Long categoryId,
    Long creditCardId
) {}

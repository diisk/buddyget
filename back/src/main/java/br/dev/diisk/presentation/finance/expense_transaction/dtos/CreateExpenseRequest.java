package br.dev.diisk.presentation.finance.expense_transaction.dtos;

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

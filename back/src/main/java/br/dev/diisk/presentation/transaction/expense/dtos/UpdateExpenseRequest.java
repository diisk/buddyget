package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;

public record UpdateExpenseRequest(
    String description,
    BigDecimal value,
    String paymentDate,
    String dueDate
) {}

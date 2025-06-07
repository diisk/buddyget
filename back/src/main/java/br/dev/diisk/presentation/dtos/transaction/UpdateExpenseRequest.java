package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

public record UpdateExpenseRequest(
    String description,
    BigDecimal value,
    String paymentDate,
    String dueDate
) {}

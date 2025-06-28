package br.dev.diisk.presentation.finance.expense_transaction.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateExpenseRequest(
        String description,
        BigDecimal value,
        LocalDateTime paymentDate,
        LocalDateTime dueDate) {
}

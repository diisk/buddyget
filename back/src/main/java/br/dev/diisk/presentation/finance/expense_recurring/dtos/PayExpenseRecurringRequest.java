package br.dev.diisk.presentation.finance.expense_recurring.dtos;

import java.time.LocalDateTime;

public record PayExpenseRecurringRequest(
                LocalDateTime paymentDate,
                LocalDateTime referenceDate) {
}

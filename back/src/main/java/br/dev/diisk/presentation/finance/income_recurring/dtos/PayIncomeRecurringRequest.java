package br.dev.diisk.presentation.finance.income_recurring.dtos;

import java.time.LocalDateTime;

public record PayIncomeRecurringRequest(
        LocalDateTime paymentDate,
        LocalDateTime referenceDate) {
}

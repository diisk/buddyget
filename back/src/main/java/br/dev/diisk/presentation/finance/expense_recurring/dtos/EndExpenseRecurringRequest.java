package br.dev.diisk.presentation.finance.expense_recurring.dtos;

import java.time.LocalDateTime;

public record EndExpenseRecurringRequest(
    LocalDateTime endDate
) {}

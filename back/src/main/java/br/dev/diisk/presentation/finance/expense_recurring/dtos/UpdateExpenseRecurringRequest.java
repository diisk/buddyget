package br.dev.diisk.presentation.finance.expense_recurring.dtos;

public record UpdateExpenseRecurringRequest(
    String description,
    String dueDay,
    String recurringDay
) {}

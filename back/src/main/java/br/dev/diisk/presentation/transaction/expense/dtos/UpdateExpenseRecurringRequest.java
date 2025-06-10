package br.dev.diisk.presentation.transaction.expense.dtos;

public record UpdateExpenseRecurringRequest(
    String description,
    String dueDay,
    String recurringDay
) {}

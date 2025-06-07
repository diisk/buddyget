package br.dev.diisk.presentation.dtos.transaction;

public record UpdateExpenseRecurringRequest(
    String description,
    String dueDay,
    String recurringDay
) {}

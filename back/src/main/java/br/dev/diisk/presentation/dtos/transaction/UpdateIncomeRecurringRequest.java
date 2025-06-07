package br.dev.diisk.presentation.dtos.transaction;

public record UpdateIncomeRecurringRequest(
    String description,
    String recurringDay
) {}

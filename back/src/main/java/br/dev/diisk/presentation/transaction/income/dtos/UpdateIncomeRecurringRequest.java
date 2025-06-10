package br.dev.diisk.presentation.transaction.income.dtos;

public record UpdateIncomeRecurringRequest(
    String description,
    String recurringDay
) {}

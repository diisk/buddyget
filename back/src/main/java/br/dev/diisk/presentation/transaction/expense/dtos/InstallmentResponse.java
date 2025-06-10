package br.dev.diisk.presentation.transaction.expense.dtos;

public record InstallmentResponse(
    Long id,
    Double amount,
    String dueDate,
    Boolean paid
) {}

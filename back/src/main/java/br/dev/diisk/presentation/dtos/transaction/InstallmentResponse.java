package br.dev.diisk.presentation.dtos.transaction;

public record InstallmentResponse(
    Long id,
    Double amount,
    String dueDate,
    Boolean paid
) {}

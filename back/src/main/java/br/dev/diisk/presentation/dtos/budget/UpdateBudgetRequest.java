package br.dev.diisk.presentation.dtos.budget;

public record UpdateBudgetRequest(
    String description,
    String observation,
    Long limitValue
) {}

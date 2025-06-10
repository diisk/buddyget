package br.dev.diisk.presentation.budget.dtos;

public record UpdateBudgetRequest(
    String description,
    String observation,
    Long limitValue
) {}

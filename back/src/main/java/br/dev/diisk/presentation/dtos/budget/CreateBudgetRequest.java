package br.dev.diisk.presentation.dtos.budget;

public record CreateBudgetRequest(
    String description,
    String observation,
    Long limitValue,
    Long categoryId
) {}

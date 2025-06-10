package br.dev.diisk.presentation.budget.dtos;

public record CreateBudgetRequest(
    String description,
    String observation,
    Long limitValue,
    Long categoryId
) {}

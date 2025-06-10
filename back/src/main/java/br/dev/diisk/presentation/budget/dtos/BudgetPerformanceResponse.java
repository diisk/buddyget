package br.dev.diisk.presentation.budget.dtos;

public record BudgetPerformanceResponse(
    Double totalSpent,
    Double totalBudgeted,
    Double performancePercentage
) {}

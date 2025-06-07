package br.dev.diisk.presentation.dtos.budget;

public record BudgetPerformanceResponse(
    Double totalSpent,
    Double totalBudgeted,
    Double performancePercentage
) {}

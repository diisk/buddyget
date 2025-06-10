package br.dev.diisk.presentation.dashboard.dtos;

import java.util.List;

public record DashboardResumeResponse(
    List<TotalCategoryExpenseResponse> totalCategoryExpense,
    List<FinanceEvolutionPointResponse> evolutionPoints
) {}

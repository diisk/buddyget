package br.dev.diisk.presentation.dtos.dashboard;

import java.util.List;

public record DashboardResumeResponse(
    List<TotalCategoryExpenseResponse> totalCategoryExpense,
    List<FinanceEvolutionPointResponse> evolutionPoints
) {}

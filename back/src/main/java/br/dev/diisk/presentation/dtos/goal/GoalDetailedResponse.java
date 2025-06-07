package br.dev.diisk.presentation.dtos.goal;

import java.util.List;
import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;
import br.dev.diisk.presentation.dtos.transaction.IncomeResponse;

public record GoalDetailedResponse(
    GoalResponse goal,
    List<ExpenseResponse> expenses,
    List<IncomeResponse> incomes
) {}

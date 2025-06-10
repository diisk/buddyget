package br.dev.diisk.presentation.goal.dtos;

import java.util.List;

import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseResponse;
import br.dev.diisk.presentation.transaction.income.dtos.IncomeResponse;

public record GoalDetailedResponse(
    GoalResponse goal,
    List<ExpenseResponse> expenses,
    List<IncomeResponse> incomes
) {}

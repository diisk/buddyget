package br.dev.diisk.presentation.goal.dtos;

import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseResponse;

public record GoalDepositResponse(
    GoalResponse goal,
    ExpenseResponse expense
) {}

package br.dev.diisk.presentation.dtos.goal;

import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;

public record GoalDepositResponse(
    GoalResponse goal,
    ExpenseResponse expense
) {}

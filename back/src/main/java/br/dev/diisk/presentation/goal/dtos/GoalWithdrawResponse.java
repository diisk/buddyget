package br.dev.diisk.presentation.goal.dtos;

import br.dev.diisk.presentation.transaction.income.dtos.IncomeResponse;

public record GoalWithdrawResponse(
    GoalResponse goal,
    IncomeResponse income
) {}

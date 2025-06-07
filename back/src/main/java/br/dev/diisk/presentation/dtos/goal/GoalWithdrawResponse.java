package br.dev.diisk.presentation.dtos.goal;

import br.dev.diisk.presentation.dtos.transaction.IncomeResponse;

public record GoalWithdrawResponse(
    GoalResponse goal,
    IncomeResponse income
) {}

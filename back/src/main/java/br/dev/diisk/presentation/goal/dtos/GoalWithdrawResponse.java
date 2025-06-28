package br.dev.diisk.presentation.goal.dtos;

import br.dev.diisk.presentation.finance.income_transaction.dtos.IncomeResponse;

public record GoalWithdrawResponse(
    GoalResponse goal,
    IncomeResponse income
) {}

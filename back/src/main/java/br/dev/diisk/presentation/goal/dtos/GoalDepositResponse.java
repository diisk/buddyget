package br.dev.diisk.presentation.goal.dtos;

import br.dev.diisk.presentation.finance.expense_transaction.dtos.ExpenseResponse;

public record GoalDepositResponse(
    GoalResponse goal,
    ExpenseResponse expense
) {}

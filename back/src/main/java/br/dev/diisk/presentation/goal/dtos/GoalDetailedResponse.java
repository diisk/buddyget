package br.dev.diisk.presentation.goal.dtos;

import java.math.BigDecimal;
import java.util.List;

import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseResponse;
import br.dev.diisk.presentation.transaction.income.dtos.IncomeResponse;

public record GoalDetailedResponse(
        Long id,
        String description,
        String dueDate,
        BigDecimal targetAmount,
        String status,
        Long categoryId,
        Double currentAmount,
        List<ExpenseResponse> expenses,
        List<IncomeResponse> incomes) {
}

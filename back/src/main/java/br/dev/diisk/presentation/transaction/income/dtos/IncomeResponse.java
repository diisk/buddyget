package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;

public record IncomeResponse(
        Long id,
        IncomeRecurringResponse incomeRecurring,
        GoalResponse goal,
        String receiptDate,
        BigDecimal value,
        String description,
        CategoryResponse category,
        String status,
        String createdAt) {
    public IncomeResponse(IncomeTransaction incomeTransaction) {
        this(
                incomeTransaction.getId(),
                incomeTransaction.getIncomeRecurring() != null
                        ? new IncomeRecurringResponse(incomeTransaction.getIncomeRecurring())
                        : null,
                incomeTransaction.getGoal() != null ? new GoalResponse(incomeTransaction.getGoal()) : null,
                incomeTransaction.getDate() != null ? incomeTransaction.getDate().toString() : null,
                incomeTransaction.getValue(),
                incomeTransaction.getDescription(),
                new CategoryResponse(incomeTransaction.getCategory()),
                incomeTransaction.getDate() != null ? "Recebido" : "Pendente",
                incomeTransaction.getCreatedAt().toString());
    }
}

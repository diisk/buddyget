package br.dev.diisk.presentation.finance.income_transaction.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.finance.income_recurring.dtos.IncomeRecurringResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;

public record IncomeResponse(
                Long id,
                IncomeRecurringResponse incomeRecurring,
                GoalResponse goal,
                String paymentDate,
                BigDecimal value,
                String description,
                CategoryResponse category,
                String status,
                String recurringReferenceDate,
                String createdAt) {
        public IncomeResponse(IncomeTransaction incomeTransaction) {
                this(
                                incomeTransaction.getId(),
                                incomeTransaction.getIncomeRecurring() != null
                                                ? new IncomeRecurringResponse(incomeTransaction.getIncomeRecurring())
                                                : null,
                                incomeTransaction.getGoal() != null ? new GoalResponse(incomeTransaction.getGoal())
                                                : null,
                                incomeTransaction.getPaymentDate() != null
                                                ? incomeTransaction.getPaymentDate().toString()
                                                : null,
                                incomeTransaction.getValue(),
                                incomeTransaction.getDescription(),
                                new CategoryResponse(incomeTransaction.getCategory()),
                                incomeTransaction.getStatus(),
                                incomeTransaction.getRecurringReferenceDate() != null
                                                ? incomeTransaction.getRecurringReferenceDate().toString()
                                                : null,
                                incomeTransaction.getCreatedAt().toString());
        }
}

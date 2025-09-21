package br.dev.diisk.application.finance.income_transaction.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.goal.Goal;

public record PendingIncomeTransactionDTO(
                Long id,
                IncomeRecurring incomeRecurring,
                Goal goal,
                String paymentDate,
                BigDecimal value,
                String description,
                Category category,
                String status,
                String recurringReferenceDate,
                String createdAt) {
        public PendingIncomeTransactionDTO(IncomeTransaction incomeTransaction) {
                this(
                                incomeTransaction.getId(),
                                incomeTransaction.getIncomeRecurring(),
                                incomeTransaction.getGoal(),
                                incomeTransaction.getPaymentDate() != null
                                                ? incomeTransaction.getPaymentDate().toString()
                                                : null,
                                incomeTransaction.getValue(),
                                incomeTransaction.getDescription(),
                                incomeTransaction.getCategory(),
                                incomeTransaction.getStatus(),
                                incomeTransaction.getRecurringReferenceDate() != null
                                                ? incomeTransaction.getRecurringReferenceDate().toString()
                                                : null,
                                incomeTransaction.getCreatedAt() != null
                                                ? incomeTransaction.getCreatedAt().toString()
                                                : null);
        }

        public PendingIncomeTransactionDTO(IncomeRecurring incomeRecurring, LocalDateTime referenceDate,
                        String status) {
                this(
                                null,
                                incomeRecurring,
                                incomeRecurring.getGoal(),
                                null,
                                incomeRecurring.getValue(),
                                incomeRecurring.getDescription(),
                                incomeRecurring.getCategory(),
                                status,
                                referenceDate.toString(),
                                referenceDate.toString());
        }
}

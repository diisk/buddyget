package br.dev.diisk.application.finance.expense_transaction.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.wish_list.WishListItem;

public record PendingExpenseTransactionDTO(
                Long id,
                String dueDate,
                ExpenseRecurring recurring,
                CreditCard creditCard,
                WishListItem wishitem,
                BigDecimal value,
                String description,
                Category category,
                String status,
                String createdAt) {
        public PendingExpenseTransactionDTO(ExpenseTransaction expenseTransaction) {
                this(
                                expenseTransaction.getId(),
                                expenseTransaction.getDueDate() != null
                                                ? expenseTransaction.getDueDate().toString()
                                                : null,
                                expenseTransaction.getExpenseRecurring(),
                                expenseTransaction.getCreditCard(),
                                expenseTransaction.getWishItem(),
                                expenseTransaction.getValue(),
                                expenseTransaction.getDescription(),
                                expenseTransaction.getCategory(),
                                expenseTransaction.getStatus(),
                                expenseTransaction.getCreatedAt() != null
                                                ? expenseTransaction.getCreatedAt().toString()
                                                : null);
        }

        public PendingExpenseTransactionDTO(ExpenseRecurring expenseRecurring, LocalDateTime referenceDate,
                        String status) {
                this(
                                null,
                                expenseRecurring.getDueDayValue() != null
                                                ? referenceDate.withDayOfMonth(expenseRecurring.getDueDayValue())
                                                                .toString()
                                                : null,
                                expenseRecurring,
                                expenseRecurring.getCreditCard(),
                                expenseRecurring.getWishItem(),
                                expenseRecurring.getValue(),
                                expenseRecurring.getDescription(),
                                expenseRecurring.getCategory(),
                                status,
                                referenceDate.toString());
        }
}

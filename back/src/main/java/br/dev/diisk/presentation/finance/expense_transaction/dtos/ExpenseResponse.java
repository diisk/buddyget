package br.dev.diisk.presentation.finance.expense_transaction.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.credit_card.dtos.CreditCardResponse;
import br.dev.diisk.presentation.finance.expense_recurring.dtos.ExpenseRecurringResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;
import br.dev.diisk.presentation.wish_list.dtos.WishListItemResponse;

public record ExpenseResponse(
                Long id,
                String dueDate,
                ExpenseRecurringResponse recurringResponse,
                CreditCardResponse creditCard,
                WishListItemResponse wishitem,
                GoalResponse goal,
                String paymentDate,
                BigDecimal value,
                String description,
                CategoryResponse category,
                String status,
                String createdAt) {
        public ExpenseResponse(ExpenseTransaction expenseTransaction) {
                this(
                                expenseTransaction.getId(),
                                expenseTransaction.getDueDate() != null
                                                ? expenseTransaction.getDueDate().toString()
                                                : null,
                                expenseTransaction.getExpenseRecurring() != null
                                                ? new ExpenseRecurringResponse(expenseTransaction.getExpenseRecurring())
                                                : null,
                                expenseTransaction.getCreditCard() != null
                                                ? new CreditCardResponse(expenseTransaction.getCreditCard(),
                                                                expenseTransaction.getPaymentDate())
                                                : null,
                                expenseTransaction.getWishItem() != null
                                                ? new WishListItemResponse(expenseTransaction.getWishItem())
                                                : null,
                                expenseTransaction.getGoal() != null
                                                ? new GoalResponse(expenseTransaction.getGoal())
                                                : null,
                                expenseTransaction.getPaymentDate() != null
                                                ? expenseTransaction.getPaymentDate().toString()
                                                : null,
                                expenseTransaction.getValue(),
                                expenseTransaction.getDescription(),
                                expenseTransaction.getCategory() != null
                                                ? new CategoryResponse(expenseTransaction.getCategory())
                                                : null,
                                expenseTransaction.getStatus(),
                                expenseTransaction.getCreatedAt() != null
                                                ? expenseTransaction.getCreatedAt().toString()
                                                : null);
        }
}

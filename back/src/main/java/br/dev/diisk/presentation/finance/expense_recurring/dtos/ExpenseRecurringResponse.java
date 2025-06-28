package br.dev.diisk.presentation.finance.expense_recurring.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.credit_card.dtos.CreditCardResponse;
import br.dev.diisk.presentation.finance.expense_transaction.dtos.InstallmentResponse;
import br.dev.diisk.presentation.wish_list.dtos.WishListItemResponse;

public record ExpenseRecurringResponse(
                Long id,
                Integer dueDay,
                InstallmentResponse installmentPlan,
                CreditCardResponse creditCard,
                WishListItemResponse wishitem,
                BigDecimal value,
                String description,
                CategoryResponse category,
                String startDate,
                String endDate,
                Integer paymentDay) {
        public ExpenseRecurringResponse(ExpenseRecurring expenseRecurring) {
                this(
                                expenseRecurring.getId(),
                                expenseRecurring.getDueDayValue(),
                                expenseRecurring.getInstallmentPlan() != null
                                                ? new InstallmentResponse(expenseRecurring.getInstallmentPlan())
                                                : null,
                                expenseRecurring.getCreditCard() != null
                                                ? new CreditCardResponse(expenseRecurring.getCreditCard(), null)
                                                : null,
                                expenseRecurring.getWishItem() != null
                                                ? new WishListItemResponse(expenseRecurring.getWishItem())
                                                : null,
                                expenseRecurring.getValue(),
                                expenseRecurring.getDescription(),
                                new CategoryResponse(expenseRecurring.getCategory()),
                                expenseRecurring.getStartDate().toString(),
                                expenseRecurring.getEndDate().toString(),
                                expenseRecurring.getPaymentDayValue());
        }
}

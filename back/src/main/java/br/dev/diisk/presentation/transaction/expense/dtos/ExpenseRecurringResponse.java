package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.transaction.expense.ExpenseRecurring;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.credit_card.dtos.CreditCardResponse;
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
        Integer recurringDay) {
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
                expenseRecurring.getWishItem() != null ? new WishListItemResponse(expenseRecurring.getWishItem())
                        : null,
                expenseRecurring.getValue(),
                expenseRecurring.getDescription(),
                new CategoryResponse(expenseRecurring.getCategory()),
                expenseRecurring.getStartDate().toString(),
                expenseRecurring.getEndDate().toString(),
                expenseRecurring.getRecurringDayValue());
    }
}

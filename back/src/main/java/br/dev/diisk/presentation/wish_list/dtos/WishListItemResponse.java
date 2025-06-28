package br.dev.diisk.presentation.wish_list.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.wish_list.WishItemPriorityEnum;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.finance.expense_recurring.dtos.ExpenseRecurringResponse;
import br.dev.diisk.presentation.finance.expense_transaction.dtos.ExpenseResponse;

public record WishListItemResponse(
                Long id,
                BigDecimal estimatedValue,
                WishItemPriorityEnum priority,
                String storeOrBrand,
                String link,
                String observation,
                CategoryResponse category,
                ExpenseResponse expense,
                ExpenseRecurringResponse expenseRecurring,
                Boolean purchased) {
        public WishListItemResponse(WishListItem wishListItem) {
                this(
                                wishListItem.getId(),
                                wishListItem.getEstimatedValue(),
                                wishListItem.getPriority(),
                                wishListItem.getStoreOrBrand(),
                                wishListItem.getLink(),
                                wishListItem.getObservation(),
                                wishListItem.getCategory() != null ? new CategoryResponse(wishListItem.getCategory())
                                                : null,
                                wishListItem.getExpenseTransaction() != null
                                                ? new ExpenseResponse(wishListItem.getExpenseTransaction())
                                                : null,
                                wishListItem.getExpenseRecurring() != null
                                                ? new ExpenseRecurringResponse(wishListItem.getExpenseRecurring())
                                                : null,
                                wishListItem.getExpenseTransaction() != null
                                                || wishListItem.getExpenseRecurring() != null);
        }
}

package br.dev.diisk.presentation.dtos.wishlist;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.wish_list.WishItemPriorityEnum;
import br.dev.diisk.presentation.dtos.transaction.ExpenseRecurringResponse;
import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;

public record WishListItemResponse(
    Long id,
    String name,
    BigDecimal estimatedValue,
    WishItemPriorityEnum priority,
    String storeOrBrand,
    String link,
    String observation,
    Long categoryId,
    ExpenseResponse expense,
    ExpenseRecurringResponse expenseRecurring,
    Boolean purchased
) {}

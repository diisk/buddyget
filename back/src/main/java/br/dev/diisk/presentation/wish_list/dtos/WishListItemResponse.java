package br.dev.diisk.presentation.wish_list.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.wish_list.WishItemPriorityEnum;
import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseRecurringResponse;
import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseResponse;

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

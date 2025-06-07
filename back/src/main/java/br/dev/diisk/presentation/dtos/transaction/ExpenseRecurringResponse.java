package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.creditcard.CreditCardResponse;
import br.dev.diisk.presentation.dtos.wishlist.WishListItemResponse;

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
    Integer recurringDay
) {}

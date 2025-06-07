package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.creditcard.CreditCardResponse;
import br.dev.diisk.presentation.dtos.goal.GoalResponse;
import br.dev.diisk.presentation.dtos.wishlist.WishListItemResponse;

public record ExpenseResponse(
    Long id,
    String dueDate,
    ExpenseRecurringResponse recurringResponse,
    CreditCardResponse creditCard,
    WishListItemResponse wishitem,
    GoalResponse goal,
    String date,
    BigDecimal value,
    String description,
    CategoryResponse category,
    String status,
    String createdAt
) {}

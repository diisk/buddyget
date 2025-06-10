package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;

import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.credit_card.dtos.CreditCardResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;
import br.dev.diisk.presentation.wish_list.dtos.WishListItemResponse;

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

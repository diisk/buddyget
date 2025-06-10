package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;

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
    Integer recurringDay
) {}

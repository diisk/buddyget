package br.dev.diisk.presentation.wish_list.dtos;

import java.math.BigDecimal;

public record WishListItemPurchaseRequest(
    Boolean isRecurring,
    BigDecimal value,
    Long creditCardId,
    String startDate,
    BigDecimal totalValue,
    Integer installmentsCount,
    String endDate,
    String dueDay,
    String recurringDay,
    String paymentDate
) {}

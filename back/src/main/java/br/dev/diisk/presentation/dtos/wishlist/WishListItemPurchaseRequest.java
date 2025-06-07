package br.dev.diisk.presentation.dtos.wishlist;

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

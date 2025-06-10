package br.dev.diisk.presentation.credit_card.dtos;

import java.math.BigDecimal;

public record PayCreditCardBillRequest(
    String description,
    String paymentDate,
    BigDecimal value,
    Long categoryId
) {}

package br.dev.diisk.presentation.dtos.creditcard;

import java.math.BigDecimal;

public record PayCreditCardBillRequest(
    String description,
    String paymentDate,
    BigDecimal value,
    Long categoryId
) {}

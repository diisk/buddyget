package br.dev.diisk.presentation.dtos.creditcard;

import java.math.BigDecimal;

public record CreditCardResponse(
    Long id,
    String name,
    String billDueDate,
    String billClosingDate,
    BigDecimal cardLimit,
    BigDecimal usedLimit,
    BigDecimal availableLimit,
    String color
) {}

package br.dev.diisk.presentation.credit_card.dtos;

import java.math.BigDecimal;

public record CreditCardDetailedResponse(
    Long id,
    String name,
    String billDueDate,
    String billClosingDate,
    BigDecimal cardLimit,
    BigDecimal usedLimit,
    BigDecimal availableLimit,
    String color
) {
    
}

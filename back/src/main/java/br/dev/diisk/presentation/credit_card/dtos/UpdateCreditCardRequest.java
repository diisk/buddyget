package br.dev.diisk.presentation.credit_card.dtos;

import java.math.BigDecimal;

public record UpdateCreditCardRequest(
    String name,
    Integer billDueDay,
    Integer billClosingDay,
    BigDecimal cardLimit,
    String color
) {}

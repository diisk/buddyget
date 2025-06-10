package br.dev.diisk.presentation.credit_card.dtos;

import java.math.BigDecimal;

public record CreateCreditCardRequest(
    String name,
    Integer billDueDay,
    Integer billClosingDay,
    BigDecimal cardLimit,
    String color
) {}

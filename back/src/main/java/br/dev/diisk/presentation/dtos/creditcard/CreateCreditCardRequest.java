package br.dev.diisk.presentation.dtos.creditcard;

import java.math.BigDecimal;

public record CreateCreditCardRequest(
    String name,
    Integer billDueDay,
    Integer billClosingDay,
    BigDecimal cardLimit,
    String color
) {}

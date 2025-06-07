package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

public record CreateIncomeRequest(
    String description,
    BigDecimal value,
    String receiptDate,
    Long categoryId
) {}

package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

public record CreateIncomeRequest(
    String description,
    BigDecimal value,
    String receiptDate,
    Long categoryId
) {}

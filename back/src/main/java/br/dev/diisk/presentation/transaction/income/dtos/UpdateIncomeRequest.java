package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

public record UpdateIncomeRequest(
    String receiptDate,
    String description,
    BigDecimal value
) {}

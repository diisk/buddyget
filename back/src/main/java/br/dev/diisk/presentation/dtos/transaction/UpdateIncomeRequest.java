package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

public record UpdateIncomeRequest(
    String receiptDate,
    String description,
    BigDecimal value
) {}

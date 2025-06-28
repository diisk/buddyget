package br.dev.diisk.presentation.finance.income_transaction.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateIncomeRequest(
        String description,
        BigDecimal value,
        LocalDateTime receiptDate,
        Long categoryId) {
}

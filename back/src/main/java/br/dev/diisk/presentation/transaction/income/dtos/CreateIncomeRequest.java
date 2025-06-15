package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateIncomeRequest(
        String description,
        BigDecimal value,
        LocalDateTime receiptDate,
        Long categoryId) {
}

package br.dev.diisk.presentation.finance.income_recurring.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateIncomeRecurringRequest(
        String description,
        LocalDateTime startDate,
        BigDecimal value,
        LocalDateTime endDate,
        Long categoryId) {
}

package br.dev.diisk.presentation.finance.expense_recurring.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateExpenseRecurringRequest(
        String description,
        LocalDateTime startDate,
        BigDecimal value,
        LocalDateTime endDate,
        Integer dueDay,
        Long categoryId,
        Long creditCardId,
        Long wishItemId) {
}

package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.transaction.income.entities.IncomeRecurring;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;

public record IncomeRecurringResponse(
        Long id,
        BigDecimal value,
        String description,
        CategoryResponse category,
        String startDate,
        String endDate,
        Integer recurringDay) {
    public IncomeRecurringResponse(IncomeRecurring incomeRecurring) {
        this(
                incomeRecurring.getId(),
                incomeRecurring.getValue(),
                incomeRecurring.getDescription(),
                new CategoryResponse(incomeRecurring.getCategory()),
                incomeRecurring.getStartDate().toString(),
                incomeRecurring.getEndDate() != null ? incomeRecurring.getEndDate().toString() : null,
                incomeRecurring.getRecurringDayValue());
    }
}

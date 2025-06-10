package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;

import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;

public record IncomeResponse(
    Long id,
    IncomeRecurringResponse incomeRecurring,
    GoalResponse goal,
    String receiptDate,
    BigDecimal value,
    String description,
    CategoryResponse category,
    String status,
    String createdAt
) {}

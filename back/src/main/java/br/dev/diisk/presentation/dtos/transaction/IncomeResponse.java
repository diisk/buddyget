package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.goal.GoalResponse;

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

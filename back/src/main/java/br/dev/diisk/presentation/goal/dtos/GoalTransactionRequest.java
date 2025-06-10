package br.dev.diisk.presentation.goal.dtos;

import java.math.BigDecimal;

public record GoalTransactionRequest(
    String description,
    BigDecimal value,
    Long categoryId
) {}

package br.dev.diisk.presentation.dtos.goal;

import java.math.BigDecimal;

public record GoalTransactionRequest(
    String description,
    BigDecimal value,
    Long categoryId
) {}

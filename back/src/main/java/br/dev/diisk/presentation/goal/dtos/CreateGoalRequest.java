package br.dev.diisk.presentation.goal.dtos;

import java.math.BigDecimal;

public record CreateGoalRequest(
    String name,
    String description,
    String dueDate,
    BigDecimal targetAmount,
    Long categoryId,
    String observation
) {}

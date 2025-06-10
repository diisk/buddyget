package br.dev.diisk.presentation.goal.dtos;

import java.math.BigDecimal;

public record UpdateGoalRequest(
    String name,
    String description,
    String dueDate,
    BigDecimal targetAmount,
    String observation
) {}

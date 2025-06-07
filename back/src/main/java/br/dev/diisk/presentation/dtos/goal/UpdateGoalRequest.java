package br.dev.diisk.presentation.dtos.goal;

import java.math.BigDecimal;

public record UpdateGoalRequest(
    String name,
    String description,
    String dueDate,
    BigDecimal targetAmount,
    String observation
) {}

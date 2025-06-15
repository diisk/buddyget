package br.dev.diisk.presentation.goal.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.goal.Goal;

public record GoalResponse(
        Long id,
        String description,
        String dueDate,
        BigDecimal targetAmount

) {
    public GoalResponse(Goal goal) {
        this(
                goal.getId(),
                goal.getDescription(),
                goal.getDueDate().toString(),
                goal.getTargetAmount());
    }
}

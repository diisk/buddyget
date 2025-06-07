package br.dev.diisk.presentation.dtos.goal;

public record GoalResponse(
    Long id,
    String name,
    String description,
    String dueDate,
    Double targetAmount,
    Double currentAmount,
    String status,
    Long categoryId
) {}

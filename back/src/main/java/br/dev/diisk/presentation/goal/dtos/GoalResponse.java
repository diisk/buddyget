package br.dev.diisk.presentation.goal.dtos;

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

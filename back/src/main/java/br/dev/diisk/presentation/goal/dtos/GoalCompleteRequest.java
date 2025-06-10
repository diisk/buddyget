package br.dev.diisk.presentation.goal.dtos;

public record GoalCompleteRequest(
    String description,
    String paymentDate,
    Long categoryId,
    Long creditCardId
) {}

package br.dev.diisk.presentation.dtos.goal;

public record GoalCompleteRequest(
    String description,
    String paymentDate,
    Long categoryId,
    Long creditCardId
) {}

package br.dev.diisk.presentation.notification;

public record NotificationResponse(
    Long id,
    String title,
    String message,
    String type,
    String status,
    String createdAt
) {}

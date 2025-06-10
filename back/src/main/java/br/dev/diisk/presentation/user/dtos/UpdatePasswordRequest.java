package br.dev.diisk.presentation.user.dtos;

public record UpdatePasswordRequest(
    String password,
    String newPassword
) {}

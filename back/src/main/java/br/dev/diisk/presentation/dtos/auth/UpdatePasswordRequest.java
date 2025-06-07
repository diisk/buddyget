package br.dev.diisk.presentation.dtos.auth;

public record UpdatePasswordRequest(
    String password,
    String newPassword
) {}

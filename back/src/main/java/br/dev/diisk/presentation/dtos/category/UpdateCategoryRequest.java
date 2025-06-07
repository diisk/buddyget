package br.dev.diisk.presentation.dtos.category;

public record UpdateCategoryRequest(
    String description,
    String name,
    String color,
    String iconName
) {}

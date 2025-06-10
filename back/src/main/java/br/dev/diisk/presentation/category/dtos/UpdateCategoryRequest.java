package br.dev.diisk.presentation.category.dtos;

public record UpdateCategoryRequest(
    String description,
    String name,
    String color,
    String iconName
) {}

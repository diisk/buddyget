package br.dev.diisk.presentation.dtos.category;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCategoryRequest {
    private String description;
    private String name;
    private String color;
    private String iconName;
}

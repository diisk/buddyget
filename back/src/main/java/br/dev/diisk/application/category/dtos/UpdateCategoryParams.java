package br.dev.diisk.application.category.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryParams {
    private String description;
    private String name;
    private String color;
    private String iconName;
}

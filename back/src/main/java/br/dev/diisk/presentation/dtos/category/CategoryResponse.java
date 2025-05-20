package br.dev.diisk.presentation.dtos.category;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String description;
    private CategoryTypeEnum type;
    private String color;
    private String iconName;
}

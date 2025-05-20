package br.dev.diisk.presentation.dtos.category;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryRequest {

    private String description;
    private String name;
    private CategoryTypeEnum type;
    private String color;
    private String iconName;
    
}

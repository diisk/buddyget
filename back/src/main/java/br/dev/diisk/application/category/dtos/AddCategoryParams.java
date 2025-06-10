package br.dev.diisk.application.category.dtos;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryParams {
    private String description;
    private String name;
    private CategoryTypeEnum type;
    private String color;
    private String iconName;
}

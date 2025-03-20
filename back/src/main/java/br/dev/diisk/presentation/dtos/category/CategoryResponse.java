package br.dev.diisk.presentation.dtos.category;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String description;
    private CategoryTypeEnum type;
    private Boolean active;
}

package br.dev.diisk.domain.filters.category;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListCategoriesFilter {
    private String searchString;
    private CategoryTypeEnum type;
}

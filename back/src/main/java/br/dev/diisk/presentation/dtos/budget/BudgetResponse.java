package br.dev.diisk.presentation.dtos.budget;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResponse {
    private Long id;
    private CategoryResponse category;
    private Long limitValue;
    private String description;
    private String observation;
}

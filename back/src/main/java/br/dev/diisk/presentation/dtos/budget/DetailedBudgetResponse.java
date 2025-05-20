package br.dev.diisk.presentation.dtos.budget;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedBudgetResponse {
    private Long id;
    private CategoryResponse category;
    private Long limitValue;
    private BigDecimal spentValue;
    private String description;
    private String observation;

}

package br.dev.diisk.presentation.dtos.dashboard;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceEvolutionPointResponse {
    private CategoryTypeEnum type;
    private Integer year;
    private Integer month;
    private BigDecimal value;
}

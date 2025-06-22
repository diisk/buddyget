package br.dev.diisk.application.monthly_summary.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMonthlySummaryValueParams {
    private Integer month;
    private Integer year;
    private BigDecimal value;
    private Category category;
}

package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IncomeRecurringResponse {
    private Long id;
    private BigDecimal value;
    private String description;
    private CategoryResponse category;
    private String startDate;
    private String endDate;
    private Integer recurringDay;
}

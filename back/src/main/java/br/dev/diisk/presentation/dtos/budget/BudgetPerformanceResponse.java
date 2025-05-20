package br.dev.diisk.presentation.dtos.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetPerformanceResponse {
    private Double totalSpent;
    private Double totalBudgeted;
    private Double performancePercentage;
}

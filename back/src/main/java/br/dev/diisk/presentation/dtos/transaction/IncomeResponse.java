package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.goal.GoalResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponse {
    private Long id;
    private IncomeRecurringResponse incomeRecurring;
    private GoalResponse goal;
    private String receiptDate;
    private BigDecimal value;
    private String description;
    private CategoryResponse category;
    private String status;//Pendente, Recebido - Baseado na data de recebimento
    private String createdAt;
}

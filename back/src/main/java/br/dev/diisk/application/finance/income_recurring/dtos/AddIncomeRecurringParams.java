package br.dev.diisk.application.finance.income_recurring.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddIncomeRecurringParams {
    private String description;
    private BigDecimal value;
    private Long categoryId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

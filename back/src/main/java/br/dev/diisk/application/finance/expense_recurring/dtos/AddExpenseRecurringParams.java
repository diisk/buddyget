package br.dev.diisk.application.finance.expense_recurring.dtos;

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
public class AddExpenseRecurringParams {
    private String description;
    private BigDecimal value;
    private Long categoryId;
    private Long wishItemId;
    private Long creditCardId;
    private Integer dueDay;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

package br.dev.diisk.application.transaction.income.dtos;

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
public class AddIncomeTransactionParams {
    private String description;
    private BigDecimal value;
    private Long categoryId;
    private LocalDateTime receiptDate;
    private Long goalId;
    private Long incomeRecurringId;
}

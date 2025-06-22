package br.dev.diisk.application.transaction.expense.dtos;

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
public class UpdateExpenseTransactionParams {
    private String description;
    private BigDecimal value;
    private LocalDateTime paymentDate;
    private LocalDateTime dueDate;
}

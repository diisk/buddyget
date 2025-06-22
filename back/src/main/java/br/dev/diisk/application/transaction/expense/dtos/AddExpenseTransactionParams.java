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
public class AddExpenseTransactionParams {
    private String description;
    private BigDecimal value;
    private Long categoryId;
    private LocalDateTime paymentDate;
    private LocalDateTime dueDate;
    private Long creditCardId;
    private Long wishItemId;
    private Long goalId;
    private Long expenseRecurringId;
}

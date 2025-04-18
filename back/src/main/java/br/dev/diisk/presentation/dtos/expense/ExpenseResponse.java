package br.dev.diisk.presentation.dtos.expense;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseResponse {

        private Long id;

        private String description;

        private Long categoryId;

        private String categoryName;

        private Long creditCardId;

        private BigDecimal amount;

        private String dueDate;

        private String paymentDate;

        private Long fixedExpenseId;
}

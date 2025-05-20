package br.dev.diisk.application.dtos.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddExpenseDto {

        private String description;

        private BigDecimal amount;

        private Long categoryId;

        private Long creditCardId;

        private Long wishItemId;TODO E TAMBÉM ADAPTAR TODOS OS DELETES COM FORCE E HIERARQUIA

        private LocalDateTime dueDate;

        private LocalDateTime paymentDate;

        private Long fixedExpenseId;

}

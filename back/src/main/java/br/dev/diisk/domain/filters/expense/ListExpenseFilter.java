package br.dev.diisk.domain.filters.expense;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ListExpenseFilter {

    private LocalDateTime startPaymentDate;
    private LocalDateTime endPaymentDate;
    private LocalDateTime startDueDate;
    private LocalDateTime endDueDate;

    private Long categoryId;
    private Long creditCardId;
    private Long fixedExpenseId;
}

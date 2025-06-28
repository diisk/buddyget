package br.dev.diisk.domain.finance.expense_transaction;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListExpenseTransactionsFilter {

        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String searchString;
}

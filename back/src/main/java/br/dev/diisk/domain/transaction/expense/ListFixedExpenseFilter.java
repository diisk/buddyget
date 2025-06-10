package br.dev.diisk.domain.transaction.expense;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListFixedExpenseFilter {

        private Boolean active;
        private Long categoryId;
}

package br.dev.diisk.presentation.dtos.goal;

import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoalDepositResponse extends GoalResponse {
    private ExpenseResponse expense;
}

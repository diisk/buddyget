package br.dev.diisk.presentation.dtos.goal;

import br.dev.diisk.presentation.dtos.transaction.IncomeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoalWithdrawResponse extends GoalResponse {
    private IncomeResponse income;
}

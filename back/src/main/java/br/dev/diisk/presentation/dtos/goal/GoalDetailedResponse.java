package br.dev.diisk.presentation.dtos.goal;

import java.util.List;

import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;
import br.dev.diisk.presentation.dtos.transaction.IncomeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoalDetailedResponse extends GoalResponse {
    private List<ExpenseResponse> expenses;
    private List<IncomeResponse> incomes;
}

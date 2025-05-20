package br.dev.diisk.presentation.dtos.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResumeResponse {
    private List<TotalCategoryExpenseResponse> totalCategoryExpense;
    private List<FinanceEvolutionPointResponse> evolutionPoints;
    //CALCULAR SALDO/GANHOS/GASTOS NO FRONT COM OS DADOS ACIMA, FILTRANDO O PERIODO
}

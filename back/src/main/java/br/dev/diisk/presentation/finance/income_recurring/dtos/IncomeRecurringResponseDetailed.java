package br.dev.diisk.presentation.finance.income_recurring.dtos;

import java.util.List;

import br.dev.diisk.presentation.finance.income_transaction.dtos.IncomeResponse;

public record IncomeRecurringResponseDetailed(
        IncomeRecurringResponse details,
        List<IncomeResponse> transactions) {
}

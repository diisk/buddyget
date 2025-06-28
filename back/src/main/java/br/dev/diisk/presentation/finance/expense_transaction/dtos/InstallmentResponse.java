package br.dev.diisk.presentation.finance.expense_transaction.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.finance.expense_recurring.InstallmentPlan;

public record InstallmentResponse(
        Integer installmentsCount,
        BigDecimal totalValue,
        BigDecimal installmentValue) {
    public InstallmentResponse(InstallmentPlan installmentPlan) {
        this(
                installmentPlan.getTotalInstallments(),
                installmentPlan.getTotalValue(),
                installmentPlan.getInstallmentValue());
    }
}

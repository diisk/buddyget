package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.transaction.expense.InstallmentPlan;

public record InstallmentResponse(
        Long id,
        Integer installmentsCount,
        BigDecimal totalValue,
        BigDecimal installmentValue) {
    public InstallmentResponse(InstallmentPlan installmentPlan) {
        this(
                installmentPlan.getId(),
                installmentPlan.getInstallmentsCount(),
                installmentPlan.getTotalValue(),
                installmentPlan.getInstallmentValue());
    }
}

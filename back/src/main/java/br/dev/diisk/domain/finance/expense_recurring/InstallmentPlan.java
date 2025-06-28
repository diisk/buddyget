package br.dev.diisk.domain.finance.expense_recurring;

import java.math.BigDecimal;
import java.util.Map;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class InstallmentPlan {

    @Column(name = "total_installments", nullable = true)
    private Integer totalInstallments;

    @Column(name = "total_value_installments", nullable = true)
    private BigDecimal totalValue;

    @Column(name = "installment_value", nullable = true)
    private BigDecimal installmentValue;

    public InstallmentPlan(Integer totalInstallments,
            BigDecimal totalValue, BigDecimal installmentValue) {
        this.totalInstallments = totalInstallments;
        this.totalValue = totalValue;
        this.installmentValue = installmentValue;
        initialize();
    }

    private void initialize() {
        if (totalInstallments != null && totalValue != null) {
            validateTotalInstallments();
            validateTotalValue();
            calculateInstallmentValue();
            return;
        }
        if (totalInstallments != null && installmentValue != null) {
            validateTotalInstallments();
            validateInstallmentValue();
            calculateTotalValue();
            return;
        }
        if (totalValue != null && installmentValue != null) {
            validateTotalValue();
            validateInstallmentValue();
            calculateTotalInstallments();
            return;
        }

        throw new BusinessException(getClass(),
                "É necessário pelo menos dois dos três valores: número de parcelas, valor total ou valor da parcela",
                null);
    }

    private void validateTotalInstallments() {
        if (totalInstallments <= 0) {
            throw new BusinessException(getClass(),
                    "O número de parcelas deve ser maior que zero",
                    Map.of("totalInstallments", totalInstallments.toString()));
        }
    }

    private void validateTotalValue() {
        if (totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(getClass(),
                    "O valor total deve ser maior que zero", Map.of("totalValue", totalValue.toString()));
        }
    }

    private void validateInstallmentValue() {
        if (installmentValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(getClass(),
                    "O valor da parcela deve ser maior que zero",
                    Map.of("installmentValue", installmentValue.toString()));
        }
    }

    private void calculateInstallmentValue() {
        this.installmentValue = totalValue.divide(BigDecimal.valueOf(totalInstallments));
    }

    private void calculateTotalValue() {
        this.totalValue = installmentValue.multiply(BigDecimal.valueOf(totalInstallments));
    }

    private void calculateTotalInstallments() {
        this.totalInstallments = totalValue.divide(installmentValue).intValue();
    }

}

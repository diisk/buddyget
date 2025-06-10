package br.dev.diisk.domain.transaction.expense;

import java.math.BigDecimal;
import java.util.Map;

import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "installment_plans")
public class InstallmentPlan extends UserRastrableEntity {

    @Column(nullable = false)
    private Integer installmentsCount;

    @Column(nullable = false)
    private BigDecimal totalValue;

    @Column(nullable = false)
    private BigDecimal installmentValue;

    public InstallmentPlan(User user, Integer installmentsCount,
            BigDecimal totalValue, BigDecimal installmentValue) {
        super(user);
        this.installmentsCount = installmentsCount;
        this.totalValue = totalValue;
        this.installmentValue = installmentValue;
        initialize();
    }

    private void initialize() {
        if (installmentsCount != null && totalValue != null) {
            validateInstallmentsCount();
            validateTotalValue();
            calculateInstallmentValue();
            return;
        }
        if (installmentsCount != null && installmentValue != null) {
            validateInstallmentsCount();
            validateInstallmentValue();
            calculateTotalValue();
            return;
        }
        if (totalValue != null && installmentValue != null) {
            validateTotalValue();
            validateInstallmentValue();
            calculateInstallmentsCount();
            return;
        }

        throw new BusinessException(getClass(),
                "É necessário pelo menos dois dos três valores: número de parcelas, valor total ou valor da parcela",
                null);
    }

    private void validateInstallmentsCount() {
        if (installmentsCount <= 0) {
            throw new BusinessException(getClass(),
                    "O número de parcelas deve ser maior que zero",
                    Map.of("installmentsCount", installmentsCount.toString()));
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
        this.installmentValue = totalValue.divide(BigDecimal.valueOf(installmentsCount));
    }

    private void calculateTotalValue() {
        this.totalValue = installmentValue.multiply(BigDecimal.valueOf(installmentsCount));
    }

    private void calculateInstallmentsCount() {
        this.installmentsCount = totalValue.divide(installmentValue).intValue();
    }

}

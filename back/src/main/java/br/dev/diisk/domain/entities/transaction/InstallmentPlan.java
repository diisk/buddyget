package br.dev.diisk.domain.entities.transaction;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
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

        throw new BadRequestValueCustomRuntimeException(
                getClass(),
                "At least two of the three values are required.",
                "installmentsCount, totalValue, installmentValue");
    }

    private void validateInstallmentsCount() {
        if (installmentsCount <= 0) {
            throw new BadRequestValueCustomRuntimeException(
                    getClass(),
                    "Installments count must be greater than zero.",
                    installmentsCount.toString());
        }
    }

    private void validateTotalValue() {
        if (totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestValueCustomRuntimeException(
                    getClass(),
                    "Total value must be greater than zero.",
                    totalValue.toString());
        }
    }

    private void validateInstallmentValue() {
        if (installmentValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestValueCustomRuntimeException(
                    getClass(),
                    "Installment value must be greater than zero.",
                    installmentValue.toString());
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

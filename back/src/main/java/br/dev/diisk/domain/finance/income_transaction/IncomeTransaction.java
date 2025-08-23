package br.dev.diisk.domain.finance.income_transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.validations.CategoryIncompatibleTypeValidation;
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.TransactionStatusEnum;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "incomes")
public class IncomeTransaction extends Transaction {

    @ManyToOne(optional = true)
    private IncomeRecurring incomeRecurring;

    public IncomeTransaction(String description, Category category, BigDecimal value, LocalDateTime date,
            User user) {
        super(description, category, value, date, user);
        validate();
    }

    public LocalDateTime getReceiptDate() {
        return this.date;
    }

    public void addIncomeRecurring(IncomeRecurring incomeRecurring, LocalDateTime recurringDate) {
        if (this.incomeRecurring != null)
            throw new BusinessException(getClass(), "A receita recorrente já foi definida");

        if (incomeRecurring == null)
            throw new NullOrEmptyException(getClass(), "incomeRecurring");

        validateIncomeRecurring(incomeRecurring);

        super.addRecurringDate(recurringDate);
        this.incomeRecurring = incomeRecurring;

    }

    public String getStatus() {
        return getReceiptDate() != null ? TransactionStatusEnum.RECEIVED.getDescription()
                : TransactionStatusEnum.PENDING.getDescription();
    }

    public void update(String description, BigDecimal value, LocalDateTime receiptDate) {
        super.update(description, value, receiptDate);
    }

    private void validateIncomeRecurring(IncomeRecurring incomeRecurring) {
        if (incomeRecurring != null && incomeRecurring.getUserId() != getUserId()) {
            throw new BusinessException(getClass(),
                    "A receita recorrente não pertence ao usuário.",
                    Map.of("incomeRecurringId", incomeRecurring.getId().toString()));
        }
    }

    private void validate() {
        new CategoryIncompatibleTypeValidation(category, CategoryTypeEnum.INCOME)
                .validate(getClass());

        validateIncomeRecurring(this.incomeRecurring);
    }

}

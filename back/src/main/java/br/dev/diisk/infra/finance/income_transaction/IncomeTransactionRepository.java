package br.dev.diisk.infra.finance.income_transaction;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.ListPaidIncomeTransactionsFilter;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class IncomeTransactionRepository extends BaseRepository<IncomeTransactionJPA, IncomeTransaction>
        implements IIncomeTransactionRepository {

    public IncomeTransactionRepository(IncomeTransactionJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<IncomeTransaction> findAllPaidBy(Long userId, ListPaidIncomeTransactionsFilter filter,
            Pageable pageable) {
        return jpa.findAllPaidBy(
                userId, filter.getStartDate(),
                filter.getEndDate(), filter.getSearchString(),
                pageable);
    }

    @Override
    public List<IncomeTransaction> findAllRecurringRelatedBy(List<Long> expenseRecurringIds) {
        return jpa.findAllByExpenseRecurring_IdInAndDeletedFalse(expenseRecurringIds);
    }

    @Override
    public List<IncomeTransaction> findAllPendingBy(Long userId) {
        return jpa.findAllByUser_IdAndDateIsNullAndDeletedFalse(userId);
    }

}

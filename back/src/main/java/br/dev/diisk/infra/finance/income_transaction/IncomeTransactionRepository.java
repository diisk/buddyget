package br.dev.diisk.infra.finance.income_transaction;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.ListIncomeTransactionsFilter;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class IncomeTransactionRepository extends BaseRepository<IncomeTransactionJPA, IncomeTransaction>
        implements IIncomeTransactionRepository {

    public IncomeTransactionRepository(IncomeTransactionJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<IncomeTransaction> findAllBy(Long userId, ListIncomeTransactionsFilter filter, Pageable pageable) {
        return jpa.findAllBy(
                userId, filter.getStartDate(),
                filter.getEndDate(), filter.getSearchString(),
                pageable);
    }

    @Override
    public Set<IncomeTransaction> findByRecurring(Long userId, Long incomeRecurringId) {
        return jpa.findAllByUser_IdAndIncomeRecurring_IdAndDeletedFalse(userId, incomeRecurringId);
    }

    @Override
    public Boolean existsByRecurring(Long incomeRecurringId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpa.existsByIncomeRecurring_IdAndRecurringReferenceDateNotNullAndRecurringReferenceDateGreaterThanEqualAndRecurringReferenceDateLessThanAndDeletedFalse(
                incomeRecurringId, startDate, endDate);
    }

}

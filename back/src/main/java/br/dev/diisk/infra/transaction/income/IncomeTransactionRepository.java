package br.dev.diisk.infra.transaction.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.transaction.income.IIncomeTransactionRepository;
import br.dev.diisk.domain.transaction.income.ListIncomeTransactionsFilter;
import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;
import br.dev.diisk.infra.shared.BaseRepository;
import br.dev.diisk.infra.transaction.income.jpas.IncomeTransactionJPA;

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

}

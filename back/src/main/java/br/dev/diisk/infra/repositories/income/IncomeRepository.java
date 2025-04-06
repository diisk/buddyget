package br.dev.diisk.infra.repositories.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.filters.income.ListIncomesFilter;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;
import br.dev.diisk.infra.jpas.income.IncomeJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class IncomeRepository extends BaseRepository<IncomeJPA, Income> implements IIncomeRepository {

    public IncomeRepository(IncomeJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<Income> findBy(Long userId, ListIncomesFilter filter, Pageable pageable) {
        if (filter.getOnlyPending())
            return jpa.findPendings(
                    userId,
                    filter.getCategoryId(),
                    pageable);

        return jpa.findReceipts(
                userId,
                filter.getStartReferenceDate(),
                filter.getEndReferenceDate(),
                filter.getCategoryId(),
                pageable);
    }

}

package br.dev.diisk.infra.finance.income_recurring;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.ListIncomeRecurringsFilter;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class IncomeRecurringRepository extends BaseRepository<IncomeRecurringJPA, IncomeRecurring>
        implements IIncomeRecurringRepository {

    public IncomeRecurringRepository(IncomeRecurringJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<IncomeRecurring> findAllBy(Long userId, ListIncomeRecurringsFilter filter, Pageable pageable) {
        return jpa.findAllBy(userId, filter.getSearchString(), pageable);
    }

    @Override
    public List<IncomeRecurring> findAllActiveBy(Long userId) {
        return jpa.findAllByUser_IdAndActiveTrueAndDeletedFalse(userId);
    }

}

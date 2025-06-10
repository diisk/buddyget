package br.dev.diisk.domain.transaction.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IIncomeRepository extends IBaseRepository<IncomeTransaction> {

    Page<IncomeTransaction> findBy(Long userId, ListIncomesFilter filter, Pageable pageable);
}

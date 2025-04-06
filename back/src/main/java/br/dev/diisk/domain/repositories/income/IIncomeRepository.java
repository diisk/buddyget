package br.dev.diisk.domain.repositories.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.filters.income.ListIncomesFilter;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface IIncomeRepository extends IBaseRepository<Income> {
    
    Page<Income> findBy(Long userId, ListIncomesFilter filter, Pageable pageable);
}
